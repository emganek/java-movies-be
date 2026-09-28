package com.gravin.MovieJava.bookings.dto;

import com.gravin.MovieJava.common.request.FilterBaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
public class GetBookingsRequest extends FilterBaseRequest {
    private LocalDate showtimeDate;

    private LocalDate canceledDate;

    private LocalDate createdDate;

    private Long userId;

    private Long movieId;
}
