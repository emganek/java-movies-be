package com.gravin.MovieJava.auth.service;

import com.gravin.MovieJava.auth.dto.AuthResponse;
import com.gravin.MovieJava.auth.dto.LoginRequest;
import com.gravin.MovieJava.auth.dto.RegisterRequest;
import com.gravin.MovieJava.users.domain.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    User register(RegisterRequest req);

    AuthResponse login(LoginRequest req);

    AuthResponse refreshToken(String cookieToken);

    AuthResponse logout(String cookieToken);
}
