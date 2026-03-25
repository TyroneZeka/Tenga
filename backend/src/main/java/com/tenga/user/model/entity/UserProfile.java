package com.tenga.user.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "usr_profiles")
public class UserProfile extends BaseEntity {

  @Column(nullable = false, unique = true)
  private UUID userId;

  @Column(length = 100)
  private String displayName;

  @Column(columnDefinition = "TEXT")
  private String bio;

  @Column(length = 500)
  private String avatarStorageKey;

  @Column(length = 500)
  private String avatarUrl;

  @Column(length = 100)
  private String city;

  @Column(length = 100)
  private String suburb;

  @Column(precision = 3, scale = 2)
  private BigDecimal trustScore;

  protected UserProfile() {}

  public UserProfile(UUID userId) {
    this.userId = userId;
  }

  public void updateDetails(String displayName, String bio, String city, String suburb) {
    this.displayName = displayName;
    this.bio = bio;
    this.city = city;
    this.suburb = suburb;
  }

  public void updateAvatar(String storageKey, String url) {
    this.avatarStorageKey = storageKey;
    this.avatarUrl = url;
  }

  public void updateTrustScore(BigDecimal score) {
    this.trustScore = score;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getBio() {
    return bio;
  }

  public String getAvatarStorageKey() {
    return avatarStorageKey;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public String getCity() {
    return city;
  }

  public String getSuburb() {
    return suburb;
  }

  public BigDecimal getTrustScore() {
    return trustScore;
  }
}
