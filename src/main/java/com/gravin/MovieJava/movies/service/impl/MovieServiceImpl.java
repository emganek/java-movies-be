package com.gravin.MovieJava.movies.service.impl;

import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.movies.domain.Movie;
import com.gravin.MovieJava.movies.dto.CreateMovieRequest;
import com.gravin.MovieJava.movies.dto.GetAllMoviesRequest;
import com.gravin.MovieJava.movies.dto.UpdateMovieRequest;
import com.gravin.MovieJava.movies.repository.MovieCustomRepository;
import com.gravin.MovieJava.movies.repository.MovieRepository;
import com.gravin.MovieJava.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepo;

    private final MovieCustomRepository movieCustomRepo;

    @Override
    public PaginationData<Movie> getAllMovies(GetAllMoviesRequest request) {
        List<Movie> movies = this.movieCustomRepo.findMoviesWithFilter(
                request.getName(),
                request.getPremiereDateFrom(),
                request.getPremiereDateTo(),
                request.getPage(),
                request.getSize()
        );

        long totalElements = this.movieCustomRepo.countMoviesWithFilter(
                request.getName(),
                request.getPremiereDateFrom(),
                request.getPremiereDateTo()
        );

        return PaginationData.of(movies, 0, 9999, totalElements); //error here
    }

    @Override
    public Movie createMovie(CreateMovieRequest req, MultipartFile posterFile) throws IOException {
        if(movieRepo.existsByCode(req.code())) {
            throw new AppException(ErrorCode.CODE_EXISTS);
        }

        Movie movie = new Movie();
        movie.setName(req.name());
        movie.setDescription(req.description());
        movie.setCode(req.code());
        movie.setIsShowing(req.isShowing());
        movie.setTrailer(req.trailer());
        movie.setPremiereDate(req.premiereDate());
        movie.setIsComing(req.isComing());
        movie.setIsHot(req.isHot());
        movie.setRating(req.rating());
        movie.setDuration(req.duration());
        movie.setPoster(posterFile.getBytes());

        return movieRepo.save(movie);
    }

    @Override
    public Movie updateMovie(
            String code, UpdateMovieRequest req, MultipartFile posterFile
    ) throws IOException {
        Movie movie = movieRepo.findByCode(code);

        if (movie == null) {
            throw new AppException(ErrorCode.CODE_NOT_FOUND);
        }

        movie.setName(req.name());
        movie.setDescription(req.description());
        movie.setCode(req.code());
        movie.setIsShowing(req.isShowing());
        movie.setTrailer(req.trailer());
        movie.setPremiereDate(req.premiereDate());
        movie.setIsComing(req.isComing());
        movie.setIsHot(req.isHot());
        movie.setRating(req.rating());
        movie.setDuration(req.duration());

        if (posterFile != null) {
            movie.setPoster(posterFile.getBytes());
        }

        return movieRepo.save(movie);
    }

    @Override
    public Optional<Movie> getMovie(String code) {
        return Optional.ofNullable(movieRepo.findByCode(code));
    }

    @Override
    public void deleteMovie(String code) {
        Movie movie = movieRepo.findByCode(code);

        if (movie == null) {
            throw new AppException(ErrorCode.CODE_NOT_FOUND);
        }

        movieRepo.delete(movie);
    }
}
