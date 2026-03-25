package com.tenga.auth.model.dto;

import com.tenga.auth.model.enums.UserRole;

public record AuthTokenResponse(
    String accessToken, String tokenType, long expiresIn, UserRole role, String userId) {

  public static AuthTokenResponse of(
      String accessToken, long expiresIn, UserRole role, String userId) {
    return new AuthTokenResponse(accessToken, "Bearer", expiresIn, role, userId);
  }
}
