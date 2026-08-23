package com.gravin.MovieJava.auth.service.impl;

import com.gravin.MovieJava.auth.dto.AuthResponse;
import com.gravin.MovieJava.auth.dto.GeneratedRefreshToken;
import com.gravin.MovieJava.auth.dto.LoginRequest;
import com.gravin.MovieJava.auth.dto.RegisterRequest;
import com.gravin.MovieJava.auth.service.AuthService;
import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.enums.UserType;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.security.jwt.JwtService;
import com.gravin.MovieJava.security.userdetails.UserPrincipal;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.dto.CreateUserRequest;
import com.gravin.MovieJava.users.mapper.UserMapper;
import com.gravin.MovieJava.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public User register(RegisterRequest req) {
        CreateUserRequest userRequest = new CreateUserRequest(
                req.username(),
                passwordEncoder.encode(req.password()),
                req.fullName(),
                req.email(),
                req.phoneNumber(),
                UserType.MEMBER
        );

        return userService.createUser(userRequest);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest req) {
        User user = userService.getUser(req.username());

        if (user == null || !passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        return issueTokenPair(user);
    }

    private AuthResponse issueTokenPair(User user) {
        UserPrincipal userPrincipal = UserPrincipal.from(user);

        String accessToken = jwtService.generateAccessToken(userPrincipal);
        GeneratedRefreshToken refreshToken = jwtService.generateRefreshToken(userPrincipal);

        return AuthResponse.of(userMapper.toUserResponse(user), accessToken, refreshToken.token(), jwtService.accessTokenTtlSeconds());
    }
}
