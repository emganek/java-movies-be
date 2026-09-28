package com.gravin.MovieJava.showtimes.repository.specification;

import com.gravin.MovieJava.showtimes.domain.Showtime;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ShowtimeSpecs {

    // Private constructure so that no one can create new instance of this class
    private ShowtimeSpecs() {}

    public static Specification<Showtime> cinemaLocationIs(Long cinemaLocationId) {
        if (cinemaLocationId == null) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) -> cb.equal(root.get("cinemaLocation").get("id"), cinemaLocationId);
    }

    public static Specification<Showtime> movieIs(Long movieId) {
        if (movieId == null) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) -> cb.equal(root.get("movie").get("id"), movieId);
    }

    public static Specification<Showtime> dateIs(LocalDate date) {
        if (date == null) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) -> cb.between(
                root.get("dateTime"),
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }

    public static Specification<Showtime> movieCodeIs(String movieCode) {
        if (movieCode == null || movieCode.isBlank()) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) -> cb.equal(root.get("movie").get("code"), movieCode);
    }
}
