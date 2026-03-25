package com.tenga.auth.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "auth_refresh_tokens")
public class RefreshToken extends BaseEntity {

  @Column(nullable = false, unique = true, length = 512)
  private String token;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private AuthUser user;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(nullable = false)
  private boolean revoked;

  @Column(length = 50)
  private String deviceInfo;

  protected RefreshToken() {}

  public RefreshToken(AuthUser user, String token, Instant expiresAt, String deviceInfo) {
    this.user = user;
    this.token = token;
    this.expiresAt = expiresAt;
    this.deviceInfo = deviceInfo;
    this.revoked = false;
  }

  public String getToken() {
    return token;
  }

  public AuthUser getUser() {
    return user;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public boolean isRevoked() {
    return revoked;
  }

  public String getDeviceInfo() {
    return deviceInfo;
  }

  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }

  public void revoke() {
    this.revoked = true;
  }
}
