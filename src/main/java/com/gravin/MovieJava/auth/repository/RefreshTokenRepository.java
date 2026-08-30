package com.gravin.MovieJava.auth.repository;

import com.gravin.MovieJava.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByJti(String jti);

    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.username = :username AND t.revoked = false")
    Integer revokeAllByName(@Param("username") String username);

    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.jti = :jti AND t.revoked = false")
    Integer revokeByJti(@Param("jti") String jti);
}
