package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.common.request.FilterBaseRequest;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetShowtimesRequest extends FilterBaseRequest {
    private Long cinemaLocationId;

    private Long movieId;

    private LocalDate date;
}
