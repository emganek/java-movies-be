package com.gravin.MovieJava.showtimes.dto;

import com.gravin.MovieJava.showtimes.domain.Seat;
import com.gravin.MovieJava.showtimes.domain.SeatType;

import java.math.BigDecimal;

public record SeatResponse(
        Long id,
        Integer number,
        SeatType type,
        BigDecimal price,
        Boolean booked
) {
    public static SeatResponse from(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getNumber(),
                seat.getType(),
                seat.getPrice(),
                seat.getBooked()
        );
    }
}
