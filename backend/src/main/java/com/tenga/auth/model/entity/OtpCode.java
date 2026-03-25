package com.tenga.auth.model.entity;

import com.tenga.auth.model.enums.OtpPurpose;
import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "auth_otp_codes")
public class OtpCode extends BaseEntity {

  @Column(nullable = false, length = 20)
  private String recipient;

  @Column(nullable = false, length = 10)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private OtpPurpose purpose;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(nullable = false)
  private boolean used;

  @Column(nullable = false)
  private int attempts;

  protected OtpCode() {}

  public OtpCode(String recipient, String code, OtpPurpose purpose, Instant expiresAt) {
    this.recipient = recipient;
    this.code = code;
    this.purpose = purpose;
    this.expiresAt = expiresAt;
    this.used = false;
    this.attempts = 0;
  }

  public String getRecipient() {
    return recipient;
  }

  public String getCode() {
    return code;
  }

  public OtpPurpose getPurpose() {
    return purpose;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public boolean isUsed() {
    return used;
  }

  public int getAttempts() {
    return attempts;
  }

  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }

  public boolean isValid() {
    return !used && !isExpired() && attempts < 3;
  }

  public void incrementAttempts() {
    this.attempts++;
  }

  public void markUsed() {
    this.used = true;
  }
}
