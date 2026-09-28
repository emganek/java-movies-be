package com.gravin.MovieJava.bookings.domain;

import com.gravin.MovieJava.bookings.enums.BookingStatus;
import com.gravin.MovieJava.showtimes.domain.Showtime;
import com.gravin.MovieJava.users.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking_entity")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtimeId", nullable = false)
    private Showtime showtime;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingSeat> bookingSeats = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private BookingStatus status;

    private Instant createdAt;

    private Instant cancelledAt;

    public void addBookingSeat(BookingSeat bookingSeat) {
        bookingSeat.setBooking(this);
        bookingSeats.add(bookingSeat);
    }
}
