package com.gravin.MovieJava.cinemalocations.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateCinemaLocationRequest(
    @NotEmpty
    String code,

    @NotEmpty
    String name,

    String address,

    @Lob
    byte[] image,

    @NotNull
    Long cinemaBrandId
) {}
