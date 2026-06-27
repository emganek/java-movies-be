package com.gravin.MovieJava.cinemalocations.dto;

public record CinemaLocationResponse(
        Long id,
        String code,
        String name,
        String address,
        String image,
        CinemaBrandSummary cinemaBrand
) {
}
