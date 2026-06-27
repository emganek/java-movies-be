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
    SHOWTIME_CONFLICTS(4010, "Showtime conflicts");

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