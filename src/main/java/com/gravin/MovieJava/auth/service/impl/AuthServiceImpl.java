package com.gravin.MovieJava.auth.service.impl;

import com.gravin.MovieJava.auth.domain.RefreshToken;
import com.gravin.MovieJava.auth.dto.AuthResponse;
import com.gravin.MovieJava.auth.dto.GeneratedRefreshToken;
import com.gravin.MovieJava.auth.dto.LoginRequest;
import com.gravin.MovieJava.auth.dto.RegisterRequest;
import com.gravin.MovieJava.auth.repository.RefreshTokenRepository;
import com.gravin.MovieJava.auth.service.AuthService;
import com.gravin.MovieJava.auth.service.RefreshTokenRevoker;
import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.enums.UserType;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.security.jwt.JwtService;
import com.gravin.MovieJava.security.userdetails.UserPrincipal;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.dto.CreateUserRequest;
import com.gravin.MovieJava.users.mapper.UserMapper;
import com.gravin.MovieJava.users.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenRevoker refreshTokenRevoker;

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final RefreshTokenRepository refreshTokenRepository;

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

    @Override
    public AuthResponse refreshToken(String cookieToken) {
        if (cookieToken == null) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Claims claims = jwtService.parseClaims(cookieToken);

        User user = userService.getUser(claims.getSubject());

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByJti(claims.getId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (refreshToken.isRevoked()) {
            refreshTokenRevoker.revokeAllForUser(user.getUsername());
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRevoker.revokeByJti(claims.getId());
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return issueTokenPair(user);
    }

    @Override
    public AuthResponse logout(String cookieToken) {
        Claims claims = jwtService.parseClaims(cookieToken);

        User user = userService.getUser(claims.getSubject());

        RefreshToken refreshToken = refreshTokenRepository.findByJti(claims.getId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (refreshToken.isRevoked()) {
            refreshTokenRevoker.revokeAllForUser(user.getUsername());
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return null;
    }

    private AuthResponse issueTokenPair(User user) {
        UserPrincipal userPrincipal = UserPrincipal.from(user);

        String accessToken = jwtService.generateAccessToken(userPrincipal);
        GeneratedRefreshToken generatedRefreshToken = jwtService.generateRefreshToken(userPrincipal);

        var refreshToken = new RefreshToken();
        refreshToken.setJti(generatedRefreshToken.jti());
        refreshToken.setUsername(user.getUsername());
        refreshToken.setExpiresAt(generatedRefreshToken.expires());
        refreshToken.setCreatedAt(generatedRefreshToken.createsAt());
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.of(userMapper.toUserResponse(user), accessToken, generatedRefreshToken.token(), jwtService.accessTokenTtlSeconds());
    }
}
