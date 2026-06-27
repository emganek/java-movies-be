package com.gravin.MovieJava.cinemabrands.mapper;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.cinemabrands.dto.CreateCinemaBrandRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CinemaBrandMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logo", source = "logo")
    CinemaBrand toEntity(CreateCinemaBrandRequest req, byte[] logo);
}
