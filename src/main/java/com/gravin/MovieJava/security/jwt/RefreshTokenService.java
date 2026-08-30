package com.gravin.MovieJava.security.jwt;

import com.gravin.MovieJava.security.config.SecurityProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final SecurityProperties securityProperties;

    public ResponseCookie build(String refreshToken) {
        return baseBuilder(refreshToken)
                .maxAge(securityProperties.jwt().refreshTokenTtl())
                .build();
    }

    public ResponseCookie clear() {
        return baseBuilder("")
                .maxAge(Duration.ZERO)
                .build();
    }

    public Optional<String> read(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request,securityProperties.refreshToken().name());

        return Optional.ofNullable(cookie)
                .map(Cookie::getValue);
    }

    public String getHeaderName() {
        return securityProperties.refreshToken().name();
    }

    private ResponseCookie.ResponseCookieBuilder baseBuilder(String token) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(securityProperties.refreshToken().name(), token)
                .sameSite(securityProperties.refreshToken().sameSite())
                .path(securityProperties.refreshToken().path())
                .secure(securityProperties.refreshToken().secure())
                .httpOnly(true);

        if (StringUtils.hasText(securityProperties.refreshToken().domain())) {
            builder.domain(securityProperties.refreshToken().domain());
        }

        return builder;
    }
}
