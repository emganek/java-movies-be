package com.gravin.MovieJava.movies.dto;

import com.gravin.MovieJava.common.util.ImageHelper;
import com.gravin.MovieJava.movies.domain.Movie;

import java.time.LocalDate;

public record MovieResponse(
        Long id,
        String name,
        String description,
        String code,
        boolean isShowing,
        String trailer,
        LocalDate premiereDate,
        boolean isComing,
        boolean isHot,
        Byte rating,
        Integer duration,
        String poster
) {
    public static MovieResponse from(Movie movie){
        return new MovieResponse(
                movie.getId(),
                movie.getName(),
                movie.getDescription(),
                movie.getCode(),
                movie.getIsShowing(),
                movie.getTrailer(),
                movie.getPremiereDate(),
                movie.getIsComing(),
                movie.getIsHot(),
                movie.getRating(),
                movie.getDuration(),
                ImageHelper.convertToDataUri(movie.getPoster())
        );
    }
}
