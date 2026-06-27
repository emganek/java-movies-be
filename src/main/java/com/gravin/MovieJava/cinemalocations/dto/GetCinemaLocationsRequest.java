package com.gravin.MovieJava.cinemalocations.dto;

import com.gravin.MovieJava.common.request.FilterBaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCinemaLocationsRequest extends FilterBaseRequest {
    private String name;

    private Long cinemaBrandId;
}
