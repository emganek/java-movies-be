package com.gravin.MovieJava.cinemalocations.repository;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CinemaLocationRepository
        extends JpaRepository<CinemaLocation, Long>, JpaSpecificationExecutor<CinemaLocation> {
    boolean existsByCode(String code);

    CinemaLocation findByCode(String code);
}
