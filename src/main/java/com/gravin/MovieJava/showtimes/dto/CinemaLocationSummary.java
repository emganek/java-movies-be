package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;

public record CinemaLocationSummary(Long id, String code, String name, String address) {
    public static CinemaLocationSummary from(CinemaLocation location) {
        return new CinemaLocationSummary(
                location.getId(),
                location.getCode(),
                location.getName(),
                location.getAddress()
        );
    }
}
