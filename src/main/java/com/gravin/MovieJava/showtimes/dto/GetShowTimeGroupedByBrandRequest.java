package com.gravin.MovieJava.showtimes.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record GetShowTimeGroupedByBrandRequest(
        String movieCode,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date
) {
}
