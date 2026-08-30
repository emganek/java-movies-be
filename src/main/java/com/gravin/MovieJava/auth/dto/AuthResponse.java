package com.gravin.MovieJava.auth.dto;

import com.gravin.MovieJava.users.dto.UserResponse;

public record AuthResponse(
        UserResponse user,
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
    public static AuthResponse of(UserResponse user, String accessToken, String refreshToken, long expiresIn) {
        return new AuthResponse(user, accessToken, refreshToken,"Bearer", expiresIn);
    }

    public static AuthResponse eliminateRefreshToken(AuthResponse response) {
        return new AuthResponse(
                response.user,
                response.accessToken,
                null,
                "Bearer",
                response.expiresIn
        );
    }
}
