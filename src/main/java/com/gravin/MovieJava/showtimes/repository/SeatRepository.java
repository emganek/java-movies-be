package com.gravin.MovieJava.showtimes.repository;

import com.gravin.MovieJava.showtimes.domain.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Modifying(flushAutomatically = true)
    @Query("""
        UPDATE VERSIONED Seat s
        SET s.booked = true
        WHERE s.showtime.id = :showtimeId
            AND s.booked = false
            AND s.id IN :seatIds
    """)
    Integer markSeatsBooked(@Param("showtimeId") Long showtimeId, @Param("seatIds")List<Long> seatIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s FROM Seat s
        WHERE s.showtime.id = :showtimeId AND s.id IN :seatIds
        ORDER BY s.id ASC
    """)
    List<Seat> lockSeatsForBooking(@Param("showtimeId") Long showtimeId, @Param("seatIds")List<Long> seatIds);

    @Modifying(flushAutomatically = true)
    @Query("""
        UPDATE VERSIONED Seat s
        SET s.booked = false
        WHERE s.showtime.id = :showtimeId
            AND s.booked = true
            AND s.id IN :seatIds
    """)
    Integer releaseSeats(@Param("showtimeId") Long showtimeId, @Param("seatIds")List<Long> seatIds);
}
