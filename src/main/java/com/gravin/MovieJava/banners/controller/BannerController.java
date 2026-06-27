package com.gravin.MovieJava.banners.controller;

import com.gravin.MovieJava.banners.dto.BannerResponse;
import com.gravin.MovieJava.banners.dto.BannerWithPoster;
import com.gravin.MovieJava.banners.service.BannerService;
import com.gravin.MovieJava.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerResponse>>> getAllBanners() {
        List<BannerResponse> banners = bannerService.getAllBannerImages()
                .stream()
                .map(b -> BannerResponse.from(b.banner(), b.poster()))
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(banners));
    }
}
