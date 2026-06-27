package com.gravin.MovieJava.showtimes.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateShowtimeRequest(
        @NotNull
        Long cinemaLocationId,

        @NotNull
        Long movieId,

        @NotNull
        LocalDateTime dateTime,

        @NotNull
        BigDecimal ticketPrice
) {
}
