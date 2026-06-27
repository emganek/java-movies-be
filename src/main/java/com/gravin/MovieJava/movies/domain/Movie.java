package com.gravin.MovieJava.movies.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="movie_entity")
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String code;

    private Boolean isShowing;

    private String trailer;

    private LocalDate premiereDate;

    private Boolean isComing;

    private Boolean isHot;

    private Byte rating;

    private Integer duration;

    @Lob
    private byte[] poster;
}
