package com.gravin.MovieJava.cinemalocations.repository.specification;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import org.springframework.data.jpa.domain.Specification;

public final class CinemaLocationSpecs {

    private CinemaLocationSpecs() {}

    public static Specification<CinemaLocation> nameLike(String name) {
        if (name == null || name.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }
        String pattern = "%" + name.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern);
    }

    public static Specification<CinemaLocation> brandIs(Long cinemaBrandId) {
        if (cinemaBrandId == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.equal(root.get("cinemaBrand").get("id"), cinemaBrandId);
    }
}
