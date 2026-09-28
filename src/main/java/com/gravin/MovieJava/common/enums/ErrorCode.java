package com.gravin.MovieJava.common.enums;

public enum ErrorCode {
    VALIDATION_FAILED(4000, "Validation failed"),
    CODE_EXISTS(4001, "Code exists"),
    USER_EXISTS(4002, "User exists"),
    USER_NOT_FOUND(4003, "User not found"),
    CODE_NOT_FOUND(4004, "Code not found"),
    CINEMA_BRAND_EXISTS(4005, "Cinema brand exists"),
    CINEMA_BRAND_NOT_FOUND(4006, "Cinema brand not found"),
    CINEMA_LOCATION_NOT_FOUND(4007, "Cinema location not found"),
    MOVIE_NOT_FOUND(4008, "Movie not found"),
    SHOWTIME_NOT_FOUND(4009, "Showtime not found"),
    SHOWTIME_CONFLICTS(4010, "Showtime conflicts"),
    UNAUTHENTICATED(4011, "Authentication required"),
    ACCESS_DENIED(4012, "You do not have permission to access this resource"),
    INVALID_CREDENTIALS(4013, "Invalid username or password"),
    INVALID_REFRESH_TOKEN(4014, "Invalid refresh token"),
    REFRESH_TOKEN_EXPIRED(4015, "Refresh token expired"),
    SHOWTIME_ALREADY_STARTED(4016, "Showtime is already started"),
    SEAT_REQUIRED(4017, "Seat is required"),
    SEAT_ALREADY_BOOKED(4018, "Seat is already booked"),
    SEAT_NOT_FOUND(4019, "At least one seat is not found"),
    BOOKING_NOT_FOUND(4020, "Booking is not found"),
    SEAT_ALREADY_CANCELED(4021, "Seat is already canceled"),
    BOOKING_PROCESSING(4022, "Booking is processing");

    private final int code;

    private final String message;

    ErrorCode( int code, String message ) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}