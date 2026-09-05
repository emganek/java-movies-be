package com.gravin.MovieJava.showtimes.service.impl;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.cinemabrands.repository.CinemaBrandRepository;
import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import com.gravin.MovieJava.cinemalocations.repository.CinemaLocationRepository;
import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.movies.domain.Movie;
import com.gravin.MovieJava.movies.repository.MovieRepository;
import com.gravin.MovieJava.movies.service.MovieService;
import com.gravin.MovieJava.showtimes.domain.Seat;
import com.gravin.MovieJava.showtimes.domain.SeatType;
import com.gravin.MovieJava.showtimes.domain.Showtime;
import com.gravin.MovieJava.showtimes.dto.*;
import com.gravin.MovieJava.showtimes.repository.ShowtimeRepository;
import com.gravin.MovieJava.showtimes.repository.specification.ShowtimeSpecs;
import com.gravin.MovieJava.showtimes.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private static final int VIP_SEAT_THRESHOLD = 20;
    private static final BigDecimal VIP_MULTIPLIER = new BigDecimal("1.1");

    private final ShowtimeRepository showtimeRepo;
    private final CinemaLocationRepository cinemaLocationRepo;
    private final CinemaBrandRepository cinemaBrandRepository;
    private final MovieRepository movieRepo;

    private final MovieService movieService;

    @Override
    @Transactional
    public Showtime createShowtime(CreateShowtimeRequest req) {
        CinemaLocation location = cinemaLocationRepo.findById(req.cinemaLocationId()).orElseThrow(() -> new AppException(ErrorCode.CINEMA_LOCATION_NOT_FOUND));

        Movie movie = movieRepo.findById(req.movieId()).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        LocalDateTime endTime = req.dateTime().plusMinutes(movie.getDuration());
        if (showtimeRepo.existsOverlap(req.movieId(), req.cinemaLocationId(), req.dateTime(), endTime)) {
            throw new AppException(ErrorCode.SHOWTIME_CONFLICTS);
        }

        Showtime showtime = new Showtime();
        showtime.setCinemaLocation(location);
        showtime.setMovie(movie);
        showtime.setDateTime(req.dateTime());
        showtime.setEndTime(endTime);
        showtime.setTicketPrice(req.ticketPrice());
        showtime.setTotalSeats(req.totalSeats());
        showtime.setSeats(generateSeats(showtime, req.ticketPrice(), req.totalSeats()));

        return showtimeRepo.save(showtime);
    }

    @Override
    public PaginationData<Showtime> getShowtimes(GetShowtimesRequest req) {
        Specification<Showtime> spec = Specification.allOf(
                ShowtimeSpecs.cinemaLocationIs(req.getCinemaLocationId()),
                ShowtimeSpecs.movieIs(req.getMovieId()),
                ShowtimeSpecs.dateIs(req.getDate()),
                ShowtimeSpecs.movieCodeIs(req.getMovieCode())
        );

        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());

        return PaginationData.from(showtimeRepo.findAll(spec, pageable));
    }

    @Override
    public Showtime getShowtime(Long id) {
        return showtimeRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
    }

    @Override
    @Transactional
    public Showtime updateShowtime(Long id, UpdateShowtimeRequest req) {
        CinemaLocation location = cinemaLocationRepo.findById(req.cinemaLocationId()).orElseThrow(() -> new AppException(ErrorCode.CINEMA_LOCATION_NOT_FOUND));

        Movie movie = movieRepo.findById(req.movieId()).orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        Showtime showtime = showtimeRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        showtime.setCinemaLocation(location);
        showtime.setMovie(movie);
        showtime.setDateTime(req.dateTime());
        showtime.setEndTime(req.dateTime().plusMinutes(movie.getDuration()));

        if (req.ticketPrice().compareTo(showtime.getTicketPrice()) != 0) {
            showtime.setTicketPrice(req.ticketPrice());
            updateSeatPrices(showtime.getSeats(), req.ticketPrice());
        }

        return showtimeRepo.save(showtime);
    }

    @Override
    public List<ShowtimesGroupedByBrandResponse> getMovieShowtimesGroupByBrand(GetShowTimeGroupedByBrandRequest req) {
        Movie movie = movieService.getMovie(req.movieCode())
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        List<Showtime> showtimes = req.date() == null
                ? showtimeRepo.findByMovieIdOrderByDateTimeAsc(movie.getId())
                : showtimeRepo.findByMovieIdForDay(movie.getId(), req.date().atStartOfDay(), req.date().plusDays(1).atStartOfDay());

//        Map<Long, List<ShowtimeResponse>> cinemaLocationIdShowtimes = showtimes.stream().collect(
//                Collectors.groupingBy(
//                        s -> s.getCinemaLocation().getId(),
//                        Collectors.mapping(ShowtimeResponse::from, Collectors.toList())
//        ) );

        Map<Long, CinemaLocationShowtimes> locationIdLocationShowtimes = showtimes.stream().collect(
                Collectors.groupingBy(
                        s -> s.getCinemaLocation().getId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                showtime -> {
                                    CinemaLocation location = showtime.getFirst().getCinemaLocation();

                                    var showtimeResponses = showtime.stream()
                                            .map(ShowtimeResponse::from)
                                            .toList();

                                    return CinemaLocationShowtimes.from(CinemaLocationSummary.from(location), showtimeResponses);
                                }
                        )
                ) );

        Set<Long> brandIds = showtimes.stream()
                .map(s -> s.getCinemaLocation().getCinemaBrand().getId())
                .collect(Collectors.toSet());

        if (brandIds.isEmpty()) {
            return List.of();
        }

        List<CinemaBrand> cinemaBrands = cinemaBrandRepository.findByIdInOrderByNameAsc(brandIds);

        return cinemaBrands.stream()
                .map(brand -> {
                    List<CinemaLocationShowtimes> locationShowtimes = new ArrayList<>();

                    locationIdLocationShowtimes.forEach((locationId, cinemaLocationShowtimes) -> {
                        if (Objects.equals(cinemaLocationShowtimes.showtimes().getFirst().cinemaLocation().cinemaBrandId(), brand.getId())) {
                            locationShowtimes.add(cinemaLocationShowtimes);
                        }
                    });

                    return ShowtimesGroupedByBrandResponse.from(brand, locationShowtimes);
                }).toList();
    }

    @Override
    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        showtimeRepo.delete(showtime);
    }

    private List<Seat> generateSeats(Showtime showtime, BigDecimal price, int totalSeats) {
        List<Seat> seats = new ArrayList<>(totalSeats);

        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setNumber(i);
            seat.setBooked(false);
            seat.setShowtime(showtime);

            if (i <= VIP_SEAT_THRESHOLD) {
                seat.setType(SeatType.VIP);
            } else {
                seat.setType(SeatType.NORMAL);
            }

            updateSeatPrices(seats, price);

            seats.add(seat);
        }

        return seats;
    }

    private void updateSeatPrices(List<Seat> seats, BigDecimal price) {
        BigDecimal vipPrice = price.multiply(VIP_MULTIPLIER);

        seats.forEach(seat -> {
            if (seat.getBooked() != true) {
                seat.setPrice(seat.getType() == SeatType.VIP ? vipPrice : price);
            }
        });
    }
}
