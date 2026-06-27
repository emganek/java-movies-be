package com.gravin.MovieJava.cinemabrands.service;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.cinemabrands.dto.CreateCinemaBrandRequest;
import com.gravin.MovieJava.cinemabrands.dto.GetAllCinemaBrandsRequest;
import com.gravin.MovieJava.cinemabrands.dto.UpdateCinemaBrandRequest;
import com.gravin.MovieJava.common.response.PaginationData;

import java.io.IOException;
import java.util.Optional;

public interface CinemaBrandService {
    PaginationData<CinemaBrand> getAllCinemaBrands(GetAllCinemaBrandsRequest request);

    CinemaBrand createCinemaBrand(CreateCinemaBrandRequest req) throws IOException;

    CinemaBrand updateCinemaBrand(Long id, UpdateCinemaBrandRequest req) throws IOException;

    Optional<CinemaBrand> getCinemaBrand(Long id);

    void deleteCinemaBrand(Long id);
}
