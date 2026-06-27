package com.gravin.MovieJava.cinemabrands.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "cinema_brand_entity")
@Getter
@Setter
public class CinemaBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String website;

    @Lob
    private byte[] logo;
}
