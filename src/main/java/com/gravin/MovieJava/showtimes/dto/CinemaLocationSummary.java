package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;

public record CinemaLocationSummary(Long id, String code, String name, String address, Long cinemaBrandId) {
    public static CinemaLocationSummary from(CinemaLocation location) {
        return new CinemaLocationSummary(
                location.getId(),
                location.getCode(),
                location.getName(),
                location.getAddress(),
                location.getCinemaBrand() == null ? null : location.getCinemaBrand().getId()
        );
    }
}
