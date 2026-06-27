package com.gravin.MovieJava.movies.service;

import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.movies.domain.Movie;
import com.gravin.MovieJava.movies.dto.CreateMovieRequest;
import com.gravin.MovieJava.movies.dto.GetAllMoviesRequest;
import com.gravin.MovieJava.movies.dto.UpdateMovieRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface MovieService {
    PaginationData<Movie> getAllMovies(GetAllMoviesRequest request);

    Movie createMovie(CreateMovieRequest req, MultipartFile posterFile) throws IOException;

    Movie updateMovie(String code, UpdateMovieRequest req, MultipartFile posterFile) throws IOException;

    Optional<Movie> getMovie(String code);

    void deleteMovie(String code);
}
