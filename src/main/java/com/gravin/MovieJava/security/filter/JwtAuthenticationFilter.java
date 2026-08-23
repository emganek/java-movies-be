package com.gravin.MovieJava.security.filter;

import com.gravin.MovieJava.security.jwt.JwtService;
import com.gravin.MovieJava.security.userdetails.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveAccessToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticate(token, request);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String token, HttpServletRequest req) {
        try {
            Claims claims = jwtService.parseClaims(token);

            if (!jwtService.getTokenType(claims).equals(JwtService.TOKEN_TYPE_ACCESS)) {
                logger.debug("Access token is required in Authorization Header");
                return;
            }

            String username = claims.getSubject();
            String userId = jwtService.getUserId(claims);

            var roles = jwtService.getUserRoles(claims);
            List<SimpleGrantedAuthority> authorities =
                    roles == null ? List.of() : roles.stream().map(SimpleGrantedAuthority::new).toList();

            UserPrincipal userPrincipal = UserPrincipal.from(Long.parseLong(userId), username, authorities);

            var authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (JwtException | IllegalArgumentException ex) {
            logger.debug("Authentication failed: " + ex);
            SecurityContextHolder.clearContext();
        }
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        }

        return null;
    }
}
