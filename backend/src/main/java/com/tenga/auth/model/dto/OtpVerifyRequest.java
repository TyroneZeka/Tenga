package com.tenga.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OtpVerifyRequest(
    @NotBlank(message = "Recipient (email or phone) is required") String recipient,
    @NotBlank(message = "OTP code is required")
        @Size(min = 4, max = 8)
        @Pattern(regexp = "^[0-9]+$", message = "OTP must be numeric")
        String code) {}
