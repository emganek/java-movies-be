package com.gravin.MovieJava.showtimes.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateShowtimeRequest(
        @NotNull
        Long cinemaLocationId,

        @NotNull
        Long movieId,

        @NotNull
        @Future
        LocalDateTime dateTime,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal ticketPrice,

        @NotNull
        @Min(1)
        @Max(500)
        Integer totalSeats
) {
}
