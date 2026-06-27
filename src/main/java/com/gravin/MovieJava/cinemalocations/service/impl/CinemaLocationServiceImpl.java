package com.gravin.MovieJava.cinemalocations.service.impl;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.cinemabrands.repository.CinemaBrandRepository;
import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import com.gravin.MovieJava.cinemalocations.dto.CreateCinemaLocationRequest;
import com.gravin.MovieJava.cinemalocations.dto.GetCinemaLocationsRequest;
import com.gravin.MovieJava.cinemalocations.dto.UpdateCinemaLocationRequest;
import com.gravin.MovieJava.cinemalocations.mapper.CinemaLocationMapper;
import com.gravin.MovieJava.cinemalocations.repository.CinemaLocationRepository;
import com.gravin.MovieJava.cinemalocations.repository.specification.CinemaLocationSpecs;
import com.gravin.MovieJava.cinemalocations.service.CinemaLocationService;
import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.common.response.PaginationData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CinemaLocationServiceImpl implements CinemaLocationService {
    private final CinemaLocationRepository cinemaLocationRepo;

    private final CinemaBrandRepository cinemaBrandRepo;

    private final CinemaLocationMapper cinemaLocationMapper;

    @Override
    public CinemaLocation createCinemaLocation(
            CreateCinemaLocationRequest req,
            MultipartFile image
    ) throws IOException {
        if (cinemaLocationRepo.existsByCode(req.code())) {
            throw new AppException(ErrorCode.CODE_EXISTS);
        }

        CinemaBrand brand = cinemaBrandRepo.findById(req.cinemaBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_BRAND_NOT_FOUND));

        CinemaLocation location = cinemaLocationMapper.toEntity(req, image.getBytes(), brand);

        return cinemaLocationRepo.save(location);
    }

    @Override
    public PaginationData<CinemaLocation> getCinemaLocations(GetCinemaLocationsRequest req) {
        Specification<CinemaLocation> spec = Specification.allOf(
                CinemaLocationSpecs.nameLike(req.getName()),
                CinemaLocationSpecs.brandIs(req.getCinemaBrandId())
        );

        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());

        return PaginationData.from(cinemaLocationRepo.findAll(spec, pageable));
    }

    @Override
    public CinemaLocation getCinemaLocation(String code) {
        return cinemaLocationRepo.findByCode(code);
    }

    @Override
    public CinemaLocation updateCinemaLocation(
            String code, UpdateCinemaLocationRequest req, MultipartFile imageFile
    ) throws IOException {
        CinemaLocation location = cinemaLocationRepo.findByCode(code);

        if (location == null) {
            throw new AppException(ErrorCode.CINEMA_LOCATION_NOT_FOUND);
        }

        CinemaBrand brand = cinemaBrandRepo.findById(req.cinemaBrandId())
                        .orElseThrow(() -> new AppException(ErrorCode.CINEMA_BRAND_NOT_FOUND));

        location.setCode(req.code());
        location.setName(req.name());
        location.setCinemaBrand(brand);

        if (imageFile != null) {
            location.setImage(imageFile.getBytes());
        }

        if (req.address() != null) {
            location.setAddress(req.address());
        }

        return cinemaLocationRepo.save(location);
    }

    @Override
    public void deleteCinemaLocation(String code) {
        CinemaLocation location = cinemaLocationRepo.findByCode(code);

        if (location == null) {
            throw new AppException(ErrorCode.CINEMA_LOCATION_NOT_FOUND);
        }

        cinemaLocationRepo.delete(location);
    }
}
