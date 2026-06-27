package com.gravin.MovieJava.common.response;

import java.time.Instant;

public record ApiResponse<T> (
        boolean success,
        String message,
        Instant timestamp,
        T data
){
    public static <D> ApiResponse<D> ok(D data) {
        return new ApiResponse<>(true, "OK", Instant.now(), data);
    }

    public static <D> ApiResponse<D> ok(String message, D data) {
        return new ApiResponse<>(true, message, Instant.now(), data);
    }
}

