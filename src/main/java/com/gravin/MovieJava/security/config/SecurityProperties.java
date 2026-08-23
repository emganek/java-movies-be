package com.gravin.MovieJava.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
    Cors cors,
    Jwt jwt
){
    public record Cors(
        List<String> allowedOrigins
    ){}

    public record Jwt(
        String secret,
        Duration accessTokenTtl,
        Duration refreshTokenTtl,
        String issuer
    ){}
}
