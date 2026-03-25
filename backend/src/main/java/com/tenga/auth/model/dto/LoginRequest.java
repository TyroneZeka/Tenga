package com.tenga.auth.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Username (email or phone) is required") String username,
    @NotBlank(message = "Password is required") String password) {}
