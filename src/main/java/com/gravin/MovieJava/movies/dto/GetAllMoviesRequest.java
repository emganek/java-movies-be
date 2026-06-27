package com.gravin.MovieJava.movies.dto;

import com.gravin.MovieJava.common.request.FilterBaseRequest;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetAllMoviesRequest extends FilterBaseRequest {
    private String name;

    private LocalDate premiereDateFrom;

    private LocalDate premiereDateTo;
}
