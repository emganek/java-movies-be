package com.gravin.MovieJava.auth.service;

import com.gravin.MovieJava.auth.dto.AuthResponse;
import com.gravin.MovieJava.auth.dto.LoginRequest;
import com.gravin.MovieJava.auth.dto.RegisterRequest;
import com.gravin.MovieJava.users.domain.User;

public interface AuthService {
    User register(RegisterRequest req);

    AuthResponse login(LoginRequest req);
}
