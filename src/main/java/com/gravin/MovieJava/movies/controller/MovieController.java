package com.gravin.MovieJava.movies.controller;

import com.gravin.MovieJava.common.response.ApiResponse;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.common.validation.annotation.FileNotEmpty;
import com.gravin.MovieJava.movies.domain.Movie;
import com.gravin.MovieJava.movies.dto.CreateMovieRequest;
import com.gravin.MovieJava.movies.dto.GetAllMoviesRequest;
import com.gravin.MovieJava.movies.dto.MovieResponse;
import com.gravin.MovieJava.movies.dto.UpdateMovieRequest;
import com.gravin.MovieJava.movies.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PutMapping
    public ResponseEntity<ApiResponse<PaginationData<MovieResponse>>> getAllMovies(
            @Valid @RequestBody(required = false) GetAllMoviesRequest request
    ) {
        PaginationData<Movie> page = movieService.getAllMovies(
                request == null ? new GetAllMoviesRequest() : request
        );

        List<MovieResponse> content = page.content().stream()
                .map(MovieResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(
                PaginationData.of(content, page.page(), page.size(), page.totalElements())
        ));
    }

    // TODO: ADD LOGIC TO HANDLE EXCEPTION
    @PostMapping
    public ResponseEntity<ApiResponse<MovieResponse>> createMovie(
            @Valid @ModelAttribute CreateMovieRequest request,
            @FileNotEmpty @RequestPart(value = "File", required = false) MultipartFile file
    ) throws IOException {
        Movie movie = movieService.createMovie(request, file);

        return ResponseEntity.ok(ApiResponse.ok(MovieResponse.from(movie)));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovie(@PathVariable String code) {
        Movie movie = movieService.getMovie(code).orElse(null);

        MovieResponse movieResponse = movie != null ? MovieResponse.from(movie) : null;

        return ResponseEntity.ok(ApiResponse.ok(movieResponse));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<MovieResponse>> updateMovie(
            @PathVariable String code,
            @Valid @ModelAttribute UpdateMovieRequest req,
            @RequestPart(value = "File", required = false) MultipartFile file
    ) throws IOException {
        Movie movie = movieService.updateMovie(code, req, file);

        return ResponseEntity.ok(ApiResponse.ok(MovieResponse.from(movie)));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<MovieResponse>> deleteMovie(@PathVariable String code) {
            movieService.deleteMovie(code);

            return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
