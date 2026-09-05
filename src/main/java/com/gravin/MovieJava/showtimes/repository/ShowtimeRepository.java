package com.gravin.MovieJava.showtimes.repository;

import com.gravin.MovieJava.showtimes.domain.Showtime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long>, JpaSpecificationExecutor<Showtime> {

    @Query("""
        SELECT COUNT(s) > 0 FROM Showtime s
        WHERE s.movie.id = :movieId
           AND s.cinemaLocation.id = :cinemaLocationId
           AND s.dateTime < :newEndTime
           AND s.endTime > :newStartTime
    """)
    boolean existsOverlap(Long movieId, Long cinemaLocationId, LocalDateTime newStartTime, LocalDateTime newEndTime);

    @EntityGraph(attributePaths = {"cinemaLocation"})
    List<Showtime> findByMovieIdOrderByDateTimeAsc(Long movieId);

    @EntityGraph(attributePaths = {"cinemaLocation"})
    @Query("""
        SELECT s FROM Showtime s
        WHERE s.movie.id = :movieId
           AND s.dateTime >= :startOfDay
           AND s.dateTime < :endOfDay
        ORDER BY s.dateTime ASC
    """)
    List<Showtime> findByMovieIdForDay(
            @Param("movieId") Long movieId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
