package com.gravin.MovieJava.banners.dto;

import com.gravin.MovieJava.banners.domain.Banner;

public record BannerWithPoster(Banner banner, byte[] poster) {
}
