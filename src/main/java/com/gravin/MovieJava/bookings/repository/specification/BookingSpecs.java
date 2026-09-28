package com.gravin.MovieJava.bookings.repository.specification;

import com.gravin.MovieJava.bookings.domain.Booking;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class BookingSpecs {

    private BookingSpecs() {}

    public static Specification<Booking> movieId(Long movieId) {
        if (movieId == null) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) ->
                cb.equal(root.get("showtime").get("movie").get("id"), movieId);
    }

    public static Specification<Booking> showtimeDateIs(LocalDate date) {
        if (date == null) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) -> cb.between(
            root.get("showtime").get("dateTime"),
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }

    public static Specification<Booking> createdDateIs(LocalDate date) {
        if (date == null) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) -> cb.between(
                root.get("createdAt"),
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }

    public static Specification<Booking> canceledDateIs(LocalDate date) {
        if (date == null) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) -> cb.between(
                root.get("cancelledAt"),
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }

    public static Specification<Booking> userIs(Long userId) {
        if (userId == null) return (root, query, cb) -> cb.conjunction();

        return (root, query, cb) -> cb.equal(
          root.get("user").get("id"), userId
        );
    }
}
