package com.tenga.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @Email(message = "Must be a valid email address") String email,
    @Pattern(
            regexp = "^\\+263[0-9]{9}$",
            message = "Phone number must be in Zimbabwe format: +263XXXXXXXXX")
        String phoneNumber,
    @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        String password,
    @NotBlank(message = "First name is required") @Size(max = 50) String firstName,
    @NotBlank(message = "Last name is required") @Size(max = 50) String lastName) {}
