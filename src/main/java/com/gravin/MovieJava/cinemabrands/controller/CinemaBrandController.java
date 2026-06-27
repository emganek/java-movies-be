package com.gravin.MovieJava.cinemabrands.controller;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import com.gravin.MovieJava.cinemabrands.dto.CinemaBrandResponse;
import com.gravin.MovieJava.cinemabrands.dto.CreateCinemaBrandRequest;
import com.gravin.MovieJava.cinemabrands.dto.GetAllCinemaBrandsRequest;
import com.gravin.MovieJava.cinemabrands.dto.UpdateCinemaBrandRequest;
import com.gravin.MovieJava.cinemabrands.service.CinemaBrandService;
import com.gravin.MovieJava.common.response.ApiResponse;
import com.gravin.MovieJava.common.response.PaginationData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/cinema-brands")
@RequiredArgsConstructor
public class CinemaBrandController {
    private final CinemaBrandService cinemaBrandService;

    @PutMapping
    public ResponseEntity<ApiResponse<PaginationData<CinemaBrandResponse>>> getAllCinemaBrands(
            @Valid @RequestBody(required = false) GetAllCinemaBrandsRequest request
    ) {
        PaginationData<CinemaBrand> page = cinemaBrandService.getAllCinemaBrands(
                request == null ? new GetAllCinemaBrandsRequest() : request
        );

        List<CinemaBrandResponse> content = page.content().stream()
                .map(CinemaBrandResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(
                PaginationData.of(content, page.page(), page.size(), page.totalElements())
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CinemaBrandResponse>> createCinemaBrand(
            @Valid @ModelAttribute CreateCinemaBrandRequest req
    ) throws IOException {
        CinemaBrand brand = cinemaBrandService.createCinemaBrand(req);

        return ResponseEntity.ok(ApiResponse.ok(CinemaBrandResponse.from(brand)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CinemaBrandResponse>> getCinemaBrand(@PathVariable Long id) {
        CinemaBrand brand = cinemaBrandService.getCinemaBrand(id).orElse(null);

        CinemaBrandResponse response = brand != null ? CinemaBrandResponse.from(brand) : null;

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CinemaBrandResponse>> updateCinemaBrand(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateCinemaBrandRequest req
    ) throws IOException {
        CinemaBrand brand = cinemaBrandService.updateCinemaBrand(id, req);

        return ResponseEntity.ok(ApiResponse.ok(CinemaBrandResponse.from(brand)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CinemaBrandResponse>> deleteCinemaBrand(@PathVariable Long id) {
        cinemaBrandService.deleteCinemaBrand(id);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
