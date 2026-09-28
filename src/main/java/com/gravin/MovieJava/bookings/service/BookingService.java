package com.gravin.MovieJava.bookings.service;

import com.gravin.MovieJava.bookings.dto.BookingResponse;
import com.gravin.MovieJava.bookings.dto.CreateBookingRequest;
import com.gravin.MovieJava.bookings.dto.GetBookingsRequest;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.security.userdetails.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(UserPrincipal userPrincipal, CreateBookingRequest req);

    PaginationData<BookingResponse> getBookings(GetBookingsRequest req);

    PaginationData<BookingResponse> getMyBookings(Long userId, @Valid GetBookingsRequest req);

    BookingResponse getBooking(Long bookingId);

    BookingResponse cancelBooking(Long bookingId);
}
