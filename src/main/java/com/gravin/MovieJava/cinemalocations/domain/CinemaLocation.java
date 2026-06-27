package com.gravin.MovieJava.cinemalocations.domain;

import com.gravin.MovieJava.cinemabrands.domain.CinemaBrand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cinema_location_entity")
@Getter
@Setter
public class CinemaLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String address;

    @Lob
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinemaBrandId", nullable = false)
    private CinemaBrand cinemaBrand;
}
