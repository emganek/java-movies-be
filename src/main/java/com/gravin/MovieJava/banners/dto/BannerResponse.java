package com.gravin.MovieJava.banners.dto;

import com.gravin.MovieJava.banners.domain.Banner;
import com.gravin.MovieJava.common.util.ImageHelper;
import com.gravin.MovieJava.movies.domain.Movie;

public record BannerResponse(
        Long id,
        Long movieId,
        String poster
) {
    public static BannerResponse from(Banner banner, Movie movie) {
        return new BannerResponse(
                banner.getId(),
                banner.getMovieId(),
                ImageHelper.convertToDataUri(movie.getPoster())
        );
    }

    public static BannerResponse from(Banner banner, byte[] poster) {
        return new BannerResponse(
                banner.getId(),
                banner.getMovieId(),
                ImageHelper.convertToDataUri(poster)
        );
    }
}
