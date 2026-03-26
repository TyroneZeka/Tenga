package com.tenga.user.model.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record UserProfileResponse(
    UUID id,
    UUID userId,
    String displayName,
    String bio,
    String avatarUrl,
    String city,
    String suburb,
    long activeListingCount,
    BigDecimal trustScore,
    Instant memberSince) {}
