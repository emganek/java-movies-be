package com.gravin.MovieJava.showtimes.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "seat_entity")
@Getter
@Setter
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;

    private SeatType type;

    private BigDecimal price;

    @Column(nullable = false)
    private Boolean booked;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtimeId", nullable = false)
    private Showtime showtime;
}
