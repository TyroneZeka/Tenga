package com.tenga.listing.model.dto;

import com.tenga.listing.model.enums.Condition;
import com.tenga.listing.model.enums.Currency;
import com.tenga.listing.model.enums.ListingStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ListingResponse(
    UUID id,
    String title,
    String description,
    BigDecimal price,
    Currency currency,
    Condition condition,
    ListingStatus status,
    UUID sellerId,
    UUID categoryId,
    String categoryName,
    String city,
    String suburb,
    boolean negotiable,
    int viewCount,
    Instant expiresAt,
    List<String> imageUrls,
    Instant createdAt,
    Instant updatedAt) {}
