package com.gravin.MovieJava.cinemabrands.repository;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;

import java.util.List;

public interface CinemaBrandCustomRepository {
    List<CinemaBrand> findCinemaBrandsWithFilter(String name, int page, int size);

    long countCinemaBrandsWithFilter(String name);
}
