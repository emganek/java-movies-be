package com.gravin.MovieJava.common.request;

import lombok.Getter;

@Getter
public class FilterBaseRequest {
    Integer page = 0;

    Integer size = 15;
}
