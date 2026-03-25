package com.tenga.auth.repository;

import com.tenga.auth.model.entity.OtpCode;
import com.tenga.auth.model.enums.OtpPurpose;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {

  @Query(
      """
            SELECT o FROM OtpCode o
            WHERE o.recipient = :recipient
              AND o.purpose = :purpose
              AND o.used = false
              AND o.expiresAt > :now
            ORDER BY o.createdAt DESC
            LIMIT 1
            """)
  Optional<OtpCode> findLatestValid(String recipient, OtpPurpose purpose, Instant now);

  @Modifying
  @Query("DELETE FROM OtpCode o WHERE o.expiresAt < :cutoff")
  void deleteExpired(Instant cutoff);
}
