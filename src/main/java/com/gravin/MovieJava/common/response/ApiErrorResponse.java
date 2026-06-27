package com.gravin.MovieJava.common.response;

import java.time.Instant;
import java.util.Optional;

public record ApiErrorResponse<T>(
        boolean success,
        int errorCode,
        String message,
        Instant timestamp,
        String path,
        Optional<T> data
) {
    public static <D> ApiErrorResponse<D> of(int errorCode, String message, String path ) {
        return new ApiErrorResponse<>(false, errorCode, message, Instant.now(), path, null);
    }

    public static <D> ApiErrorResponse<D> of(int errorCode, String message, String path, D data ) {
        return new ApiErrorResponse<>(false, errorCode, message, Instant.now(), path, Optional.of(data));
    }
}
