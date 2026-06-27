package com.gravin.MovieJava.movies.repository;

import com.gravin.MovieJava.movies.domain.Movie;

import java.time.LocalDate;
import java.util.List;

public interface MovieCustomRepository {
    List<Movie> findMoviesWithFilter(String name, LocalDate premiereDateFrom, LocalDate premiereDateTo, int page, int size);

    long countMoviesWithFilter(String name, LocalDate premiereDateFrom, LocalDate premiereDateTo);
}
