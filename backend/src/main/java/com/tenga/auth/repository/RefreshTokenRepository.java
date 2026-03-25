package com.tenga.auth.repository;

import com.tenga.auth.model.entity.RefreshToken;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

  Optional<RefreshToken> findByToken(String token);

  @Modifying
  @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
  void revokeAllByUserId(UUID userId);

  @Modifying
  @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :cutoff OR rt.revoked = true")
  void deleteExpiredAndRevoked(Instant cutoff);
}
