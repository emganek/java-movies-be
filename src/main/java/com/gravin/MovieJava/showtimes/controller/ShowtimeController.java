package com.gravin.MovieJava.showtimes.controller;

import com.gravin.MovieJava.common.response.ApiResponse;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.showtimes.domain.Showtime;
import com.gravin.MovieJava.showtimes.dto.*;
import com.gravin.MovieJava.showtimes.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShowtimeResponse>> createShowtime(
            @Valid @RequestBody CreateShowtimeRequest req
    ) {
        Showtime showtime = showtimeService.createShowtime(req);

        return ResponseEntity.ok(ApiResponse.ok(ShowtimeResponse.from(showtime)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PaginationData<ShowtimeResponse>>> getShowtimes(
            @RequestBody GetShowtimesRequest req
    ) {
        PaginationData<Showtime> page = showtimeService.getShowtimes(req);

        List<ShowtimeResponse> content = page.content().stream()
                .map(ShowtimeResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(
                PaginationData.of(content, page.page(), page.size(), page.totalElements())
        ));
    }

    @GetMapping("/{showtimeId}")
    public ResponseEntity<ApiResponse<ShowtimeResponse>> getShowtime(
            @PathVariable Long showtimeId
    ) {
        Showtime showtime = showtimeService.getShowtime(showtimeId);

        return ResponseEntity.ok(ApiResponse.ok(ShowtimeResponse.from(showtime)));
    }

    @PutMapping("/{showtimeId}")
    public ResponseEntity<ApiResponse<ShowtimeResponse>> updateShowtime(
            @PathVariable Long showtimeId,
            @Valid @RequestBody UpdateShowtimeRequest req
    ) {
        Showtime showtime = showtimeService.updateShowtime(showtimeId, req);

        return ResponseEntity.ok(ApiResponse.ok(ShowtimeResponse.from(showtime)));
    }

    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<ApiResponse<ShowtimeResponse>> deleteShowtime(
            @PathVariable Long showtimeId
    ) {
        showtimeService.deleteShowtime(showtimeId);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
