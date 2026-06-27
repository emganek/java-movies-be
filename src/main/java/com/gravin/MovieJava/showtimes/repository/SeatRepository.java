package com.gravin.MovieJava.showtimes.repository;

import com.gravin.MovieJava.showtimes.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
