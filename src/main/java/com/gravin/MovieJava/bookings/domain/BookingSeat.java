package com.gravin.MovieJava.bookings.domain;

import com.gravin.MovieJava.showtimes.domain.Seat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "booking_seat_entity",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_booking_seat_active",
                columnNames ={"seatId", "activeFlag"}
        )
)
@Getter
@Setter
public class BookingSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingId", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seatId", nullable = false)
    private Seat seat;

    private Integer seatNumber;

    private BigDecimal price;

    private Boolean activeFlag;
}
