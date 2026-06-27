package com.gravin.MovieJava.showtimes.domain;

import com.gravin.MovieJava.cinemalocations.domain.CinemaLocation;
import com.gravin.MovieJava.movies.domain.Movie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "showtime_entity")
@Getter
@Setter
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinemaLocationId", nullable = false)
    private CinemaLocation cinemaLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", nullable = false)
    private Movie movie;

    private LocalDateTime dateTime;

    private LocalDateTime endTime;

    private BigDecimal ticketPrice;

    private Integer totalSeats;

    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();
}
