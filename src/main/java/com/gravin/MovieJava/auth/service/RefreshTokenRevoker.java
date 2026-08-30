package com.gravin.MovieJava.auth.service;

import com.gravin.MovieJava.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Revocations that must survive the caller's rollback.
 *
 * <p>{@code AuthService.refresh} rejects a bad token by throwing, which rolls back its
 * transaction — so a revoke written inside that transaction would be discarded along
 * with it. These methods run in their own transaction ({@code REQUIRES_NEW}) and commit
 * independently of the caller's outcome.
 *
 * <p>This lives in a separate bean on purpose: {@code REQUIRES_NEW} is applied by the
 * Spring proxy, so calling such a method on {@code this} would silently do nothing.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenRevoker {

    private final RefreshTokenRepository refreshTokenRepository;

    /** Revokes a single token by its {@code jti}. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeByJti(String jti) {
        refreshTokenRepository.revokeByJti(jti);
    }

    /** Revokes every live token for the user — the response to refresh-token reuse. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeAllForUser(String username) {
        refreshTokenRepository.revokeAllByName(username);
    }
}
