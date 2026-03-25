package com.tenga.auth.service;

import com.tenga.auth.model.dto.AuthTokenResponse;
import com.tenga.auth.model.dto.LoginRequest;
import com.tenga.auth.model.dto.OtpVerifyRequest;
import com.tenga.auth.model.dto.RefreshTokenRequest;
import com.tenga.auth.model.dto.RegisterRequest;
import com.tenga.auth.model.entity.AuthUser;
import com.tenga.auth.model.entity.OtpCode;
import com.tenga.auth.model.entity.RefreshToken;
import com.tenga.auth.model.enums.AuthProvider;
import com.tenga.auth.model.enums.OtpPurpose;
import com.tenga.auth.model.enums.UserRole;
import com.tenga.auth.repository.AuthUserRepository;
import com.tenga.auth.repository.OtpCodeRepository;
import com.tenga.auth.repository.RefreshTokenRepository;
import com.tenga.common.exception.BusinessException;
import com.tenga.common.exception.ConflictException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
  private static final String REFRESH_TOKEN_COOKIE = "refresh_token";

  private final AuthUserRepository authUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final OtpCodeRepository otpCodeRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Value("${tenga.jwt.refresh-token-expiry}")
  private long refreshTokenExpirySeconds;

  public AuthServiceImpl(
      AuthUserRepository authUserRepository,
      RefreshTokenRepository refreshTokenRepository,
      OtpCodeRepository otpCodeRepository,
      JwtService jwtService,
      PasswordEncoder passwordEncoder) {
    this.authUserRepository = authUserRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.otpCodeRepository = otpCodeRepository;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void register(RegisterRequest request) {
    if (request.email() != null && authUserRepository.existsByEmail(request.email())) {
      throw new ConflictException("An account with this email already exists");
    }
    if (request.phoneNumber() != null
        && authUserRepository.existsByPhoneNumber(request.phoneNumber())) {
      throw new ConflictException("An account with this phone number already exists");
    }

    String passwordHash = passwordEncoder.encode(request.password());
    AuthUser user =
        new AuthUser(
            request.email(),
            request.phoneNumber(),
            passwordHash,
            UserRole.BUYER,
            AuthProvider.LOCAL);

    authUserRepository.save(user);
    log.info("New user registered: userId={}", user.getId());

    // Trigger OTP for phone verification if phone provided
    if (request.phoneNumber() != null) {
      sendOtp(request.phoneNumber(), OtpPurpose.PHONE_VERIFICATION);
    } else if (request.email() != null) {
      sendOtp(request.email(), OtpPurpose.EMAIL_VERIFICATION);
    }
  }

  @Override
  @Transactional
  public AuthTokenResponse login(
      LoginRequest request, String deviceInfo, HttpServletResponse response) {
    AuthUser user =
        authUserRepository
            .findActiveByEmailOrPhone(request.username())
            .orElseThrow(() -> new BusinessException("Invalid credentials"));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new BusinessException("Invalid credentials");
    }

    user.recordLogin();
    String accessToken = jwtService.generateAccessToken(user);
    String rawRefreshToken = generateSecureToken();
    Instant refreshExpiry = Instant.now().plusSeconds(refreshTokenExpirySeconds);

    RefreshToken refreshToken = new RefreshToken(user, rawRefreshToken, refreshExpiry, deviceInfo);
    refreshTokenRepository.save(refreshToken);

    setRefreshTokenCookie(response, rawRefreshToken);
    log.info("User logged in: userId={}", user.getId());

    return AuthTokenResponse.of(
        accessToken,
        jwtService.getAccessTokenExpirySeconds(),
        user.getRole(),
        user.getId().toString());
  }

  @Override
  @Transactional
  public void verifyOtp(OtpVerifyRequest request, OtpPurpose purpose) {
    OtpCode otp =
        otpCodeRepository
            .findLatestValid(request.recipient(), purpose, Instant.now())
            .orElseThrow(() -> new BusinessException("OTP not found or expired"));

    otp.incrementAttempts();
    if (!otp.isValid()) {
      throw new BusinessException("OTP has expired or too many attempts");
    }
    if (!otp.getCode().equals(request.code())) {
      throw new BusinessException("Invalid OTP code");
    }

    otp.markUsed();

    // Mark the relevant field as verified on the auth user
    authUserRepository
        .findByEmail(request.recipient())
        .ifPresent(
            u -> {
              if (purpose == OtpPurpose.EMAIL_VERIFICATION) u.markEmailVerified();
            });
    authUserRepository
        .findByPhoneNumber(request.recipient())
        .ifPresent(
            u -> {
              if (purpose == OtpPurpose.PHONE_VERIFICATION) u.markPhoneVerified();
            });

    log.info("OTP verified: recipient={}, purpose={}", maskRecipient(request.recipient()), purpose);
  }

  @Override
  @Transactional
  public AuthTokenResponse refreshToken(RefreshTokenRequest request, HttpServletResponse response) {
    RefreshToken existing =
        refreshTokenRepository
            .findByToken(request.refreshToken())
            .orElseThrow(() -> new BusinessException("Invalid or expired refresh token"));

    if (existing.isRevoked() || existing.isExpired()) {
      throw new BusinessException("Refresh token is no longer valid");
    }

    existing.revoke();

    AuthUser user = existing.getUser();
    String accessToken = jwtService.generateAccessToken(user);
    String newRawToken = generateSecureToken();
    Instant newExpiry = Instant.now().plusSeconds(refreshTokenExpirySeconds);

    RefreshToken newRefreshToken =
        new RefreshToken(user, newRawToken, newExpiry, existing.getDeviceInfo());
    refreshTokenRepository.save(newRefreshToken);

    setRefreshTokenCookie(response, newRawToken);

    return AuthTokenResponse.of(
        accessToken,
        jwtService.getAccessTokenExpirySeconds(),
        user.getRole(),
        user.getId().toString());
  }

  @Override
  @Transactional
  public void logout(String refreshToken) {
    refreshTokenRepository
        .findByToken(refreshToken)
        .ifPresent(
            rt -> {
              rt.revoke();
              log.info("User logged out: userId={}", rt.getUser().getId());
            });
  }

  @Override
  @Transactional
  public void sendOtp(String recipient, OtpPurpose purpose) {
    // Generate 6-digit OTP
    String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));
    Instant expiresAt = Instant.now().plusSeconds(300); // 5 minutes

    OtpCode otp = new OtpCode(recipient, code, purpose, expiresAt);
    otpCodeRepository.save(otp);

    // Notification dispatch happens via Kafka event (not implemented here yet)
    log.info("OTP generated for recipient={}, purpose={}", maskRecipient(recipient), purpose);
  }

  // --- private helpers ---

  private String generateSecureToken() {
    return UUID.randomUUID().toString().replace("-", "")
        + UUID.randomUUID().toString().replace("-", "");
  }

  private void setRefreshTokenCookie(HttpServletResponse response, String token) {
    Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, token);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/api/v1/auth");
    cookie.setMaxAge((int) refreshTokenExpirySeconds);
    response.addCookie(cookie);
  }

  private String maskRecipient(String recipient) {
    if (recipient == null || recipient.length() < 4) return "****";
    // Mask phone: +263 77* *** **45 style
    if (recipient.startsWith("+263")) {
      return recipient.substring(0, 7) + "****" + recipient.substring(recipient.length() - 2);
    }
    // Mask email: first 2 chars + *** + @domain
    int atIndex = recipient.indexOf('@');
    if (atIndex > 2) {
      return recipient.substring(0, 2) + "***" + recipient.substring(atIndex);
    }
    return "****";
  }
}
