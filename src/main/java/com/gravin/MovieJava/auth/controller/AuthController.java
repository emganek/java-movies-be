package com.gravin.MovieJava.auth.controller;

import com.gravin.MovieJava.auth.dto.AuthResponse;
import com.gravin.MovieJava.auth.dto.LoginRequest;
import com.gravin.MovieJava.auth.dto.RegisterRequest;
import com.gravin.MovieJava.auth.service.AuthService;
import com.gravin.MovieJava.common.response.ApiResponse;
import com.gravin.MovieJava.users.dto.CreateUserResponse;
import com.gravin.MovieJava.users.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final UserMapper userMapper;

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

        return ResponseEntity.ok(ApiResponse.ok(authResponse));
    }
}
