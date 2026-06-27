package com.gravin.MovieJava.cinemalocations.mapper;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import com.gravin.MovieJava.cinemalocations.dto.CinemaBrandSummary;
import com.gravin.MovieJava.cinemalocations.dto.CinemaLocationResponse;
import com.gravin.MovieJava.cinemalocations.dto.CreateCinemaLocationRequest;
import com.gravin.MovieJava.common.util.ImageHelper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CinemaLocationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "req.code")
    @Mapping(target = "name", source = "req.name")
    @Mapping(target = "address", source = "req.address")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "cinemaBrand", source = "cinemaBrand")
    CinemaLocation toEntity(CreateCinemaLocationRequest req, byte[] image, CinemaBrand cinemaBrand);

    @Mapping(target = "image", source = "image", qualifiedByName = "toDataUri")
    CinemaLocationResponse toResponse(CinemaLocation location);

    CinemaBrandSummary toBrandSummary(CinemaBrand brand);

    @Named("toDataUri")
    default String convertToDataUri(byte[] bytes) {
        return ImageHelper.convertToDataUri(bytes);
    }
}
