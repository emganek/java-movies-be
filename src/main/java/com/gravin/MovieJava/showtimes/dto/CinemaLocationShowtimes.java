package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import com.gravin.MovieJava.common.util.ImageHelper;
import com.gravin.MovieJava.showtimes.domain.Showtime;

import java.util.List;

public record CinemaLocationShowtimes(
        Long id,
        String name,
        String address,
        String image,
        List<ShowtimeResponse> showtimes
) {
    public static CinemaLocationShowtimes from(
            CinemaLocationSummary location, List<ShowtimeResponse> showtimes
    ) {
        return new CinemaLocationShowtimes(
                location.id(),
                location.name(),
                location.address(),
                location.image(),
                showtimes
        );
    }
}
