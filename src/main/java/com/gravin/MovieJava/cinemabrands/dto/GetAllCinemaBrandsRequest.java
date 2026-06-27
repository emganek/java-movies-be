package com.gravin.MovieJava.cinemabrands.dto;

import com.gravin.MovieJava.common.request.FilterBaseRequest;
import lombok.Getter;

@Getter
public class GetAllCinemaBrandsRequest extends FilterBaseRequest {
    private String name;
}
