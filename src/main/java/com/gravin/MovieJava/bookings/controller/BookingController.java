package com.gravin.MovieJava.bookings.controller;

import com.gravin.MovieJava.bookings.dto.BookingResponse;
import com.gravin.MovieJava.bookings.dto.CreateBookingRequest;
import com.gravin.MovieJava.bookings.dto.GetBookingsRequest;
import com.gravin.MovieJava.bookings.service.BookingService;
import com.gravin.MovieJava.common.response.ApiResponse;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.security.userdetails.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateBookingRequest req
    ) {
        var booking = bookingService.createBooking(userPrincipal, req);

        return ResponseEntity.ok(ApiResponse.ok(booking));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PaginationData<BookingResponse>>> getBookings(
            @Valid @RequestBody GetBookingsRequest req
    ) {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getBookings(req)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<PaginationData<BookingResponse>>> getMyBookings(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody GetBookingsRequest req
    ) {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getMyBookings(userPrincipal.getId(), req)));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBooking(
            @PathVariable Long bookingId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getBooking(bookingId)));
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long bookingId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.cancelBooking(bookingId)));
    }
}
