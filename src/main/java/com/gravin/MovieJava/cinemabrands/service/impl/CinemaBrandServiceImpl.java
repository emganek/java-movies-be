package com.gravin.MovieJava.cinemabrands.service.impl;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.cinemabrands.dto.CreateCinemaBrandRequest;
import com.gravin.MovieJava.cinemabrands.dto.GetAllCinemaBrandsRequest;
import com.gravin.MovieJava.cinemabrands.dto.UpdateCinemaBrandRequest;
import com.gravin.MovieJava.cinemabrands.mapper.CinemaBrandMapper;
import com.gravin.MovieJava.cinemabrands.repository.CinemaBrandCustomRepository;
import com.gravin.MovieJava.cinemabrands.repository.CinemaBrandRepository;
import com.gravin.MovieJava.cinemabrands.service.CinemaBrandService;
import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.common.response.PaginationData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CinemaBrandServiceImpl implements CinemaBrandService {
    private final CinemaBrandRepository cinemaBrandRepo;

    private final CinemaBrandCustomRepository cinemaBrandCustomRepo;

    private final CinemaBrandMapper cinemaBrandMapper;

    @Override
    public PaginationData<CinemaBrand> getAllCinemaBrands(GetAllCinemaBrandsRequest request) {
        List<CinemaBrand> brands = cinemaBrandCustomRepo.findCinemaBrandsWithFilter(
                request.getName(),
                request.getPage(),
                request.getSize()
        );

        long totalElements = cinemaBrandCustomRepo.countCinemaBrandsWithFilter(request.getName());

        return PaginationData.of(brands, request.getPage(), request.getSize(), totalElements);
    }

    @Override
    public CinemaBrand createCinemaBrand(CreateCinemaBrandRequest req) throws IOException {
        if (cinemaBrandRepo.existsByName(req.name())) {
            throw new AppException(ErrorCode.CINEMA_BRAND_EXISTS);
        }

        byte[] logo = req.logo().getBytes();
        CinemaBrand brand = cinemaBrandMapper.toEntity(req, logo);

        return cinemaBrandRepo.save(brand);
    }

    @Override
    public CinemaBrand updateCinemaBrand(Long id, UpdateCinemaBrandRequest req) throws IOException {
        CinemaBrand brand = cinemaBrandRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_BRAND_NOT_FOUND));

        brand.setName(req.name());
        brand.setWebsite(req.website());

        if (req.logo() != null && !req.logo().isEmpty()) {
            brand.setLogo(req.logo().getBytes());
        }

        return cinemaBrandRepo.save(brand);
    }

    @Override
    public Optional<CinemaBrand> getCinemaBrand(Long id) {
        return cinemaBrandRepo.findById(id);
    }

    @Override
    public void deleteCinemaBrand(Long id) {
        CinemaBrand brand = cinemaBrandRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_BRAND_NOT_FOUND));

        cinemaBrandRepo.delete(brand);
    }
}
