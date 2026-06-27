package com.gravin.MovieJava.cinemalocations.service;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import com.gravin.MovieJava.cinemalocations.dto.CreateCinemaLocationRequest;
import com.gravin.MovieJava.cinemalocations.dto.GetCinemaLocationsRequest;
import com.gravin.MovieJava.cinemalocations.dto.UpdateCinemaLocationRequest;
import com.gravin.MovieJava.common.response.PaginationData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CinemaLocationService {
    CinemaLocation createCinemaLocation(CreateCinemaLocationRequest req, MultipartFile image) throws IOException;

    PaginationData<CinemaLocation> getCinemaLocations(GetCinemaLocationsRequest req);

    CinemaLocation getCinemaLocation(String code);

    CinemaLocation updateCinemaLocation(String code, UpdateCinemaLocationRequest req, MultipartFile file) throws IOException;

    void deleteCinemaLocation(String code);
}
