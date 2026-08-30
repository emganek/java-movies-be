package com.gravin.MovieJava.auth.controller;

import com.gravin.MovieJava.auth.dto.AuthResponse;
import com.gravin.MovieJava.auth.dto.LoginRequest;
import com.gravin.MovieJava.auth.dto.RegisterRequest;
import com.gravin.MovieJava.auth.service.AuthService;
import com.gravin.MovieJava.common.response.ApiResponse;
import com.gravin.MovieJava.security.jwt.RefreshTokenService;
import com.gravin.MovieJava.users.dto.CreateUserResponse;
import com.gravin.MovieJava.users.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final UserMapper userMapper;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("register")
    ResponseEntity<ApiResponse<CreateUserResponse>> register(
            @Valid @RequestBody RegisterRequest req
    ) {
        var user = authService.register(req);

        return ResponseEntity.ok(ApiResponse.ok(userMapper.toCreateUserResponse(user)));
    }

    @PostMapping("login")
    ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest req
    ) {
        AuthResponse authResponse = authService.login(req);

        return responseWithRefreshToken(authResponse);
    }

    @PostMapping("refresh-token")
    ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            HttpServletRequest req
    ) {
        var cookieToken = refreshTokenService.read(req)
                .orElse(null);

        var authResponse = authService.refreshToken(cookieToken);

        return responseWithRefreshToken(authResponse);
    }

    @PostMapping("logout")
    ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest req) {
        var cookieToken = refreshTokenService.read(req)
                .orElse(null);

        authService.logout(cookieToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenService.clear().toString())
                .body(ApiResponse.ok("Logged out", null));
    }

    private ResponseEntity<ApiResponse<AuthResponse>> responseWithRefreshToken(AuthResponse authResponse) {
        ResponseCookie cookie = refreshTokenService.build(authResponse.refreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.ok(AuthResponse.eliminateRefreshToken(authResponse)));

    }
}
