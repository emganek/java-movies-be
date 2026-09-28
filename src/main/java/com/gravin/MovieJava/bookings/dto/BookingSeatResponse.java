package com.gravin.MovieJava.bookings.dto;

import com.gravin.MovieJava.bookings.domain.BookingSeat;

import java.math.BigDecimal;

public record BookingSeatResponse(
        Long bookingId,
        Long seatId,
        Integer number,
        BigDecimal price
) {
    public static BookingSeatResponse from(BookingSeat bookingSeat) {
        return new BookingSeatResponse(
                bookingSeat.getBooking().getId(),
                bookingSeat.getSeat().getId(),
                bookingSeat.getSeatNumber(),
                bookingSeat.getPrice()
        );
    }
}
