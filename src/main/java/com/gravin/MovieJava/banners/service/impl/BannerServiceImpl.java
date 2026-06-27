package com.gravin.MovieJava.banners.service.impl;

import com.gravin.MovieJava.banners.domain.Banner;
import com.gravin.MovieJava.banners.dto.BannerWithPoster;
import com.gravin.MovieJava.banners.repository.BannerRepository;
import com.gravin.MovieJava.banners.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepo;

    @Override
    public List<BannerWithPoster> getAllBannerImages() {
        return this.bannerRepo.getAllBannerImages()
                .stream()
                .map(result -> new BannerWithPoster((Banner) result[0], (byte[]) result[1]))
                .toList();
    }
}
