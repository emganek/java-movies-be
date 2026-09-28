package com.gravin.MovieJava.bookings.service.impl;

import com.gravin.MovieJava.bookings.domain.Booking;
import com.gravin.MovieJava.bookings.domain.BookingSeat;
import com.gravin.MovieJava.bookings.dto.BookingResponse;
import com.gravin.MovieJava.bookings.dto.CreateBookingRequest;
import com.gravin.MovieJava.bookings.dto.GetBookingsRequest;
import com.gravin.MovieJava.bookings.enums.BookingStatus;
import com.gravin.MovieJava.bookings.repository.BookingRepository;
import com.gravin.MovieJava.bookings.repository.specification.BookingSpecs;
import com.gravin.MovieJava.bookings.service.BookingService;
import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.security.userdetails.UserPrincipal;
import com.gravin.MovieJava.showtimes.domain.Seat;
import com.gravin.MovieJava.showtimes.domain.Showtime;
import com.gravin.MovieJava.showtimes.service.SeatService;
import com.gravin.MovieJava.showtimes.service.ShowtimeService;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepo;

    private final UserService userService;

    private final SeatService seatService;

    private final ShowtimeService showtimeService;

    @Transactional
    @Override
    public BookingResponse createBooking(UserPrincipal userPrincipal, CreateBookingRequest req) {
        User user = userService.getUser(userPrincipal.getUsername());

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Showtime showtime = showtimeService.getShowtime(req.showtimeId());

        if (showtime == null) {
            throw new AppException(ErrorCode.SHOWTIME_NOT_FOUND);
        } else if (showtime.getDateTime().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.SHOWTIME_ALREADY_STARTED);
        }

        List<Long> seatIds = req.seatIds().stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();

        if (seatIds.isEmpty()) {
            throw new AppException(ErrorCode.SEAT_REQUIRED);
        }

        List<Seat> seats;

        try {
            seats = seatService.lockSeatsForBooking(showtime.getId(), seatIds);
        } catch (PessimisticLockingFailureException ex) {
            log.debug("Could not lock seats {} of showtime {}: {}", seatIds, showtime.getId(), ex.getMessage());
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }

        if (seats.size() != seatIds.size()) {
            throw new AppException(ErrorCode.SEAT_NOT_FOUND);
        }

        if (
                seats.stream().anyMatch( seat -> Boolean.TRUE.equals(seat.getBooked()))
        ) {
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }

        var booking = new Booking();
        booking.setUser(user);
        booking.setShowtime(showtime);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCreatedAt(Instant.now());

        seats.forEach( seat -> {
            var bookingSeat = new BookingSeat();
            bookingSeat.setSeat(seat);
            bookingSeat.setSeatNumber(seat.getNumber());
            bookingSeat.setPrice(seat.getPrice());
            bookingSeat.setActiveFlag(Boolean.TRUE);

            booking.addBookingSeat(bookingSeat);
        } );

        Integer updatedSeatsNumber = seatService.markSeatsBooked(showtime.getId(), seatIds);
        if (updatedSeatsNumber != seatIds.size()) {
            log.debug("Seat race lost for showtime {}: expected {} seats, updated {}",
                    showtime.getId(), seatIds.size(), updatedSeatsNumber);
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }

        try {
            // Guard 3 - flushed here so the unique index verdict arrives inside this method
            // and can be reported as a clean domain error rather than a 500.
            return BookingResponse.from(bookingRepo.saveAndFlush(booking));
        } catch (DataIntegrityViolationException ex) {
            log.debug("Unique constraint rejected booking for seats {}: {}", seatIds, ex.getMessage());
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }
    }

    @Override
    public PaginationData<BookingResponse> getBookings(GetBookingsRequest req) {
        return findBookings(req.getUserId(), req);
    }

    @Override
    public PaginationData<BookingResponse> getMyBookings(Long userId, GetBookingsRequest req) {
        return findBookings(userId, req);
    }

    @Override
    public BookingResponse getBooking(Long bookingId) {
        return bookingRepo.findById(bookingId)
                .map(BookingResponse::from)
                .orElse(null);
    }

    @Transactional
    @Override
    public BookingResponse cancelBooking(Long bookingId) {
        var booking = bookingRepo.findById(bookingId).orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new AppException(ErrorCode.SEAT_ALREADY_CANCELED);
        }

        var bookingSeatIds = booking.getBookingSeats().stream()
                .map(bookingSeat -> bookingSeat.getSeat().getId())
                .sorted()
                .toList();

        List<Seat> seats;;

        try {
            seats = seatService.lockSeatsForBooking(booking.getShowtime().getId(), bookingSeatIds);
        } catch (PessimisticLockingFailureException ex) {
            log.debug("Seat race lost for booking cancel {}: expected {} seats",
                    bookingId, booking.getBookingSeats());
            throw new AppException(ErrorCode.BOOKING_PROCESSING);
        }

        if (seats.size() != bookingSeatIds.size()) {
            throw new AppException(ErrorCode.SEAT_NOT_FOUND);
        }

        seatService.releaseSeats(booking.getShowtime().getId(),bookingSeatIds);

        booking.getBookingSeats().forEach( bookingSeat -> bookingSeat.setActiveFlag(false));
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(Instant.now());

        try {
            return BookingResponse.from(bookingRepo.saveAndFlush(booking));
        } catch (DataIntegrityViolationException ex) {
            log.debug("Unique constraint rejected cancel for seats {}: {}", bookingSeatIds, ex.getMessage());
            throw new AppException(ErrorCode.SEAT_ALREADY_BOOKED);
        }
    }

    private PaginationData<BookingResponse> findBookings(Long userId, GetBookingsRequest req) {
        Specification<Booking> spec = Specification.allOf(
                BookingSpecs.canceledDateIs(req.getCanceledDate()),
                BookingSpecs.createdDateIs(req.getCreatedDate()),
                BookingSpecs.showtimeDateIs(req.getShowtimeDate()),
                BookingSpecs.movieId(req.getMovieId()),
                BookingSpecs.userIs(userId)
        );

        Sort sort = Sort.by(Sort.Direction.ASC, "showtime.dateTime");
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);

        var bookingsPage = bookingRepo.findAll(spec, pageable);

        var bookingResponses = bookingsPage.getContent().stream()
                .map(BookingResponse::from)
                .toList();

        return PaginationData.of(
                bookingResponses,
                bookingsPage.getPageable().getPageNumber(),
                bookingsPage.getSize(),
                bookingsPage.getTotalElements()
        );
    }
}
