package com.tenga.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Display name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String displayName,
    @NotBlank(message = "Phone number is required")
        @Pattern(
            regexp = "^\\+263[0-9]{9}$",
            message = "Phone number must be in Zimbabwe format: +263XXXXXXXXX")
        String phone,
    @Email(message = "Must be a valid email address") String email,
    @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        String password) {}
