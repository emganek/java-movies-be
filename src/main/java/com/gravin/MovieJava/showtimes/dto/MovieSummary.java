package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.movies.domain.Movie;

public record MovieSummary(Long id, String name, String code) {
    public static MovieSummary from(Movie movie) {
        return new MovieSummary(movie.getId(), movie.getName(), movie.getCode());
    }
}
