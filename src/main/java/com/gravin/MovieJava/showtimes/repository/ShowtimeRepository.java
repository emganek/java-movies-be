package com.gravin.MovieJava.showtimes.repository;

import com.gravin.MovieJava.showtimes.domain.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long>, JpaSpecificationExecutor<Showtime> {

    @Query("""
        SELECT COUNT(s) > 0 FROM Showtime s
        WHERE s.movie.id = :movieId
        AND s.cinemaLocation.id = :cinemaLocationId
        AND s.dateTime < :newEndTime
        AND s.endTime > :newStartTime
    """)
    boolean existsOverlap(Long movieId, Long cinemaLocationId, LocalDateTime newStartTime, LocalDateTime newEndTime);
}
