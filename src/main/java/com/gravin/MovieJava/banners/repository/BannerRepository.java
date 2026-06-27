package com.gravin.MovieJava.banners.repository;

import com.gravin.MovieJava.banners.domain.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("""
        SELECT b, m.poster
        FROM Banner b
        JOIN Movie m ON m.id = b.movieId
    """)
    List<Object[]> getAllBannerImages();
}
