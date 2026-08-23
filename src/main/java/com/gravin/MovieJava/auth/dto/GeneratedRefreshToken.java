package com.gravin.MovieJava.auth.dto;

import java.time.Instant;

public record GeneratedRefreshToken(
        String jti,

        String token,

        Instant createsAt,

        Instant expires
) {
}
