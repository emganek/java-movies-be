package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import com.gravin.MovieJava.common.util.ImageHelper;

public record CinemaLocationSummary(Long id, String code, String name, String address, String image,Long cinemaBrandId) {
    public static CinemaLocationSummary from(CinemaLocation location) {
        return new CinemaLocationSummary(
                location.getId(),
                location.getCode(),
                location.getName(),
                location.getAddress(),
                ImageHelper.convertToDataUri(location.getImage()),
                location.getCinemaBrand() == null ? null : location.getCinemaBrand().getId()
        );
    }
}
