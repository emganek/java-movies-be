package com.gravin.MovieJava.movies.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateMovieRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "description is required")
        String description,

        @NotBlank( message = "Code is required")
        String code,

        @NotNull(message = "Now showing is required")
        boolean isShowing,

        @NotBlank(message = "Trailer link is required")
        String trailer,

        LocalDate premiereDate,

        @NotNull(message = "Is coming is required")
        boolean isComing,

        @NotNull(message = "Is hot is required")
        boolean isHot,

        @NotNull(message = "Rating is required")
        Byte rating,

        Integer duration,

        String poster
) {}
