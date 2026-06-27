package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.showtimes.domain.Showtime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ShowtimeResponse(
        Long id,
        MovieSummary movie,
        CinemaLocationSummary cinemaLocation,
        LocalDateTime dateTime,
        BigDecimal ticketPrice,
        Integer totalSeats,
        List<SeatResponse> seats
) {
    public static ShowtimeResponse from(Showtime showtime) {
        return new ShowtimeResponse(
                showtime.getId(),
                MovieSummary.from(showtime.getMovie()),
                CinemaLocationSummary.from(showtime.getCinemaLocation()),
                showtime.getDateTime(),
                showtime.getTicketPrice(),
                showtime.getTotalSeats(),
                showtime.getSeats().stream().map(SeatResponse::from).toList()
        );
    }
}
