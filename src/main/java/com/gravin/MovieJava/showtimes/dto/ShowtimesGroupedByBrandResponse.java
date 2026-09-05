package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.common.util.ImageHelper;

import java.util.List;

public record ShowtimesGroupedByBrandResponse(
        Long id,

        String name,

        String website,

        String logo,

        List<CinemaLocationShowtimes> cinemaLocation
) {
    public static ShowtimesGroupedByBrandResponse from (
            CinemaBrand brand,
            List<CinemaLocationShowtimes> cinemaLocation
    ) {
        return new ShowtimesGroupedByBrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getWebsite(),
                ImageHelper.convertToDataUri(brand.getLogo()),
                cinemaLocation
        );
    }
}
