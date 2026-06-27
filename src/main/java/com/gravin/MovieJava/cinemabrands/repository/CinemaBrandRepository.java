package com.gravin.MovieJava.cinemabrands.repository;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaBrandRepository extends JpaRepository<CinemaBrand, Long> {
    boolean existsByName(String name);
}
