package com.tenga.auth.service;

import com.tenga.auth.model.dto.AuthTokenResponse;
import com.tenga.auth.model.dto.LoginRequest;
import com.tenga.auth.model.dto.OtpVerifyRequest;
import com.tenga.auth.model.dto.RefreshTokenRequest;
import com.tenga.auth.model.dto.RegisterRequest;
import com.tenga.auth.model.enums.OtpPurpose;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

  void register(RegisterRequest request);

  AuthTokenResponse login(LoginRequest request, String deviceInfo, HttpServletResponse response);

  void verifyOtp(OtpVerifyRequest request, OtpPurpose purpose);

  AuthTokenResponse refreshToken(RefreshTokenRequest request, HttpServletResponse response);

  void logout(String refreshToken);

  void sendOtp(String recipient, OtpPurpose purpose);
}
