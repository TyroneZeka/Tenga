package com.tenga.auth.model.entity;

import com.tenga.auth.model.enums.AuthProvider;
import com.tenga.auth.model.enums.UserRole;
import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "auth_users")
public class AuthUser extends BaseEntity {

  @Column(unique = true, length = 100)
  private String email;

  @Column(unique = true, length = 20)
  private String phoneNumber;

  @Column(length = 120)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private UserRole role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private AuthProvider provider;

  @Column(length = 200)
  private String providerId;

  @Column(nullable = false)
  private boolean emailVerified;

  @Column(nullable = false)
  private boolean phoneVerified;

  @Column(nullable = false)
  private boolean enabled;

  private Instant lastLoginAt;

  private Instant deletedAt;

  protected AuthUser() {}

  public AuthUser(
      String email, String phoneNumber, String passwordHash, UserRole role, AuthProvider provider) {
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.passwordHash = passwordHash;
    this.role = role;
    this.provider = provider;
    this.emailVerified = false;
    this.phoneVerified = false;
    this.enabled = true;
  }

  public String getEmail() {
    return email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public UserRole getRole() {
    return role;
  }

  public AuthProvider getProvider() {
    return provider;
  }

  public String getProviderId() {
    return providerId;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public boolean isPhoneVerified() {
    return phoneVerified;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public Instant getLastLoginAt() {
    return lastLoginAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public void markEmailVerified() {
    this.emailVerified = true;
  }

  public void markPhoneVerified() {
    this.phoneVerified = true;
  }

  public void recordLogin() {
    this.lastLoginAt = Instant.now();
  }

  public void disable() {
    this.enabled = false;
  }

  public void softDelete() {
    this.deletedAt = Instant.now();
    this.enabled = false;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }
}
