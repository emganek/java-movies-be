package com.gravin.MovieJava.showtimes.service;

import com.gravin.MovieJava.showtimes.domain.Seat;

import java.util.List;

public interface SeatService {
    Integer markSeatsBooked(Long showtimeId, List<Long> seatIds);

    Integer releaseSeats(Long showtimeId, List<Long> seatIds);

    List<Seat> lockSeatsForBooking(Long showtimeId, List<Long> seatIds);
}
