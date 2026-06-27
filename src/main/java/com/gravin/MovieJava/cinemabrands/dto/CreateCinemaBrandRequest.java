package com.gravin.MovieJava.cinemabrands.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateCinemaBrandRequest(
        @NotNull
        String name,

        @NotNull
        String website,

        MultipartFile logo
) {
}
