package com.gravin.MovieJava.bookings.dto;

import com.gravin.MovieJava.bookings.domain.Booking;
import com.gravin.MovieJava.bookings.domain.BookingSeat;
import com.gravin.MovieJava.bookings.enums.BookingStatus;
import com.gravin.MovieJava.showtimes.domain.Showtime;
import com.gravin.MovieJava.showtimes.dto.ShowtimeResponse;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public record BookingResponse(
        Long id,
        ShowtimeResponse showtime,
        BookingStatus status,
        Instant createAt,
        Instant cancelledAt,
        List<BookingSeatResponse> seats
) {
    public static BookingResponse from(Booking booking) {
        List<BookingSeatResponse> seats = booking.getBookingSeats().stream()
                .map(BookingSeatResponse::from)
                .sorted(Comparator.comparing(BookingSeatResponse::number))
                .toList();

        return new BookingResponse(
                booking.getId(),
                ShowtimeResponse.from(booking.getShowtime()),
                booking.getStatus(),
                booking.getCreatedAt(),
                booking.getCancelledAt(),
                seats
        );
    }
}
