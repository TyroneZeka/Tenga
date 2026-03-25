package com.tenga.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tenga.auth.model.dto.RegisterRequest;
import com.tenga.auth.repository.AuthUserRepository;
import com.tenga.auth.repository.OtpCodeRepository;
import com.tenga.auth.repository.RefreshTokenRepository;
import com.tenga.common.exception.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @Mock private AuthUserRepository authUserRepository;

  @Mock private RefreshTokenRepository refreshTokenRepository;

  @Mock private OtpCodeRepository otpCodeRepository;

  @Mock private JwtService jwtService;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private AuthServiceImpl authService;

  @Test
  void should_throwConflictException_when_emailAlreadyExists() {
    RegisterRequest request =
        new RegisterRequest("taken@example.com", null, "Password1!", "John", "Doe");

    when(authUserRepository.existsByEmail("taken@example.com")).thenReturn(true);

    assertThatThrownBy(() -> authService.register(request))
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("email");

    verify(authUserRepository, never()).save(any());
  }

  @Test
  void should_throwConflictException_when_phoneAlreadyExists() {
    RegisterRequest request =
        new RegisterRequest(null, "+263771234567", "Password1!", "Jane", "Doe");

    when(authUserRepository.existsByPhoneNumber("+263771234567")).thenReturn(true);

    assertThatThrownBy(() -> authService.register(request))
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("phone");

    verify(authUserRepository, never()).save(any());
  }
}
