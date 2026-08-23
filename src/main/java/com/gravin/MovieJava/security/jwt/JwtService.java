package com.gravin.MovieJava.security.jwt;

import com.gravin.MovieJava.auth.dto.GeneratedRefreshToken;
import com.gravin.MovieJava.security.config.SecurityProperties;
import com.gravin.MovieJava.security.userdetails.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {
    public static final String TOKEN_TYPE_ACCESS = "access";

    public static final String TOKEN_TYPE_REFRESH = "refresh";

    private static final String CLAIM_TOKEN_TYPE = "token_type";

    private static final String CLAIM_ROLE = "role";

    private static final String CLAIM_UID = "uid";

    private final SecurityProperties securityProperties;

    private final SecretKey signingKey;
    
    JwtService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.signingKey = Keys.hmacShaKeyFor(
            securityProperties.jwt().secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateAccessToken(UserPrincipal userPrincipal) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(securityProperties.jwt().accessTokenTtl());

        var authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .issuer(securityProperties.jwt().issuer())
                .subject(userPrincipal.getUsername())
                .id(UUID.randomUUID().toString())
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                .claim(CLAIM_UID, String.valueOf(userPrincipal.getId()))
                .claim(CLAIM_ROLE, String.join(",", authorities))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public GeneratedRefreshToken generateRefreshToken(UserPrincipal userPrincipal) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(securityProperties.jwt().refreshTokenTtl());
        String jti = UUID.randomUUID().toString();

        String refreshToken = Jwts.builder()
                .issuer(securityProperties.jwt().issuer())
                .subject(userPrincipal.getUsername())
                .id(jti)
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH)
                .claim(CLAIM_UID, userPrincipal.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();

        return new GeneratedRefreshToken(jti, refreshToken, now, expiresAt);
    }

    public long accessTokenTtlSeconds() {
        return securityProperties.jwt().accessTokenTtl().toSeconds();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(securityProperties.jwt().issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getTokenType(Claims claims) {
        return claims.get(JwtService.CLAIM_TOKEN_TYPE, String.class);
    }

    public String getUserId(Claims claims) {
        return claims.get(JwtService.CLAIM_UID, String.class);
    }

    public List<String> getUserRoles(Claims claims) {
        String authorities = claims.get(JwtService.CLAIM_ROLE, String.class);

        return authorities == null || authorities.isBlank() ? List.of() : List.of(authorities.split(","));
    }
}
