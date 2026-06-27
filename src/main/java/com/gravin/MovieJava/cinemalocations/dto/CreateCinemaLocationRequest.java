package com.gravin.MovieJava.cinemalocations.dto;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCinemaLocationRequest(
        @NotBlank
        String code,

        @NotBlank
        String name,

        String address,

        @NotNull
        Long cinemaBrandId
) {
}
