package com.tenga.review.model.dto;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponse(
    UUID id,
    UUID revieweeId,
    UUID reviewerId,
    UUID listingId,
    int rating,
    String comment,
    Instant createdAt) {}
