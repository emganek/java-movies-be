package com.gravin.MovieJava.cinemalocations.controller;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import com.gravin.MovieJava.cinemalocations.dto.CinemaLocationResponse;
import com.gravin.MovieJava.cinemalocations.dto.CreateCinemaLocationRequest;
import com.gravin.MovieJava.cinemalocations.dto.GetCinemaLocationsRequest;
import com.gravin.MovieJava.cinemalocations.dto.UpdateCinemaLocationRequest;
import com.gravin.MovieJava.cinemalocations.mapper.CinemaLocationMapper;
import com.gravin.MovieJava.cinemalocations.service.CinemaLocationService;
import com.gravin.MovieJava.common.response.ApiResponse;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.common.validation.annotation.FileNotEmpty;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/cinema-locations")
@RequiredArgsConstructor
public class CinemaLocationController {
    private final CinemaLocationService cinemaLocationService;

    private final CinemaLocationMapper cinemaLocationMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CinemaLocationResponse>> createCinemaLocation(
            @Valid @ModelAttribute CreateCinemaLocationRequest req,
            @FileNotEmpty @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        CinemaLocation location = cinemaLocationService.createCinemaLocation(req, image);

        return ResponseEntity.ok(ApiResponse.ok(cinemaLocationMapper.toResponse(location)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PaginationData<CinemaLocationResponse>>> getCinemaLocations(
            @Valid @RequestBody GetCinemaLocationsRequest req
    ) {
        PaginationData<CinemaLocation> page = cinemaLocationService.getCinemaLocations(req);

        List<CinemaLocationResponse> content = page.content().stream()
                .map(cinemaLocationMapper::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(
                PaginationData.of(content, page.page(), page.size(), page.totalElements())
        ));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<CinemaLocationResponse>> getCinemaLocation(
            @PathVariable String code
    ) {
        CinemaLocation location = cinemaLocationService.getCinemaLocation(code);

        return ResponseEntity.ok(ApiResponse.ok(cinemaLocationMapper.toResponse(location)));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<CinemaLocationResponse>> updateCinemaLocation(
            @PathVariable String code,
            @Valid @ModelAttribute UpdateCinemaLocationRequest req,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {
        CinemaLocation location = cinemaLocationService.updateCinemaLocation(code, req, imageFile);

        return ResponseEntity.ok(ApiResponse.ok(cinemaLocationMapper.toResponse(location)));
    }

    public ResponseEntity<ApiResponse<CinemaLocationResponse>> deleteCinemaLocation(
            @PathVariable String code
    ) {
        cinemaLocationService.deleteCinemaLocation(code);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}