package com.gravin.MovieJava.movies.repository;

import com.gravin.MovieJava.movies.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByCode(String code);

    Movie findByCode(String code);
}
