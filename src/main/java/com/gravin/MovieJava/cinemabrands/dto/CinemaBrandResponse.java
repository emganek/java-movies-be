package com.gravin.MovieJava.cinemabrands.dto;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.common.util.ImageHelper;

public record CinemaBrandResponse(
        Long id,
        String name,
        String website,
        String logo
) {
    public static CinemaBrandResponse from(CinemaBrand brand) {
        return new CinemaBrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getWebsite(),
                ImageHelper.convertToDataUri(brand.getLogo())
        );
    }
}
