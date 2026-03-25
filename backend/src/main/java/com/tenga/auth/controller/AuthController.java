package com.tenga.auth.controller;

import com.tenga.auth.model.dto.AuthTokenResponse;
import com.tenga.auth.model.dto.LoginRequest;
import com.tenga.auth.model.dto.OtpVerifyRequest;
import com.tenga.auth.model.dto.RefreshTokenRequest;
import com.tenga.auth.model.dto.RegisterRequest;
import com.tenga.auth.model.enums.OtpPurpose;
import com.tenga.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Registration, login, OTP, and token management")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  @Operation(summary = "Register a new user account")
  public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  @Operation(summary = "Login with email/phone and password")
  public ResponseEntity<AuthTokenResponse> login(
      @Valid @RequestBody LoginRequest request,
      @RequestHeader(value = "X-Device-Info", required = false) String deviceInfo,
      HttpServletResponse response) {
    return ResponseEntity.ok(authService.login(request, deviceInfo, response));
  }

  @PostMapping("/otp/verify")
  @Operation(summary = "Verify an OTP code (phone or email verification)")
  public ResponseEntity<Void> verifyOtp(
      @Valid @RequestBody OtpVerifyRequest request,
      @RequestHeader(value = "X-Otp-Purpose", defaultValue = "PHONE_VERIFICATION")
          String purposeHeader) {
    OtpPurpose purpose = OtpPurpose.valueOf(purposeHeader.toUpperCase());
    authService.verifyOtp(request, purpose);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh")
  @Operation(summary = "Exchange a refresh token for a new access token")
  public ResponseEntity<AuthTokenResponse> refresh(
      @CookieValue(value = "refresh_token", required = false) String cookieToken,
      @RequestBody(required = false) RefreshTokenRequest bodyRequest,
      HttpServletResponse response) {
    String token =
        cookieToken != null
            ? cookieToken
            : (bodyRequest != null ? bodyRequest.refreshToken() : null);
    if (token == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(authService.refreshToken(new RefreshTokenRequest(token), response));
  }

  @PostMapping("/logout")
  @Operation(summary = "Invalidate the current refresh token")
  public ResponseEntity<Void> logout(
      @CookieValue(value = "refresh_token", required = false) String cookieToken,
      @RequestBody(required = false) RefreshTokenRequest bodyRequest) {
    String token =
        cookieToken != null
            ? cookieToken
            : (bodyRequest != null ? bodyRequest.refreshToken() : null);
    if (token != null) {
      authService.logout(token);
    }
    return ResponseEntity.ok().build();
  }
}
