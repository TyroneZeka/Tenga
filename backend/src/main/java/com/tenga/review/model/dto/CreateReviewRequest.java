package com.tenga.review.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateReviewRequest(
    @NotNull UUID transactionId,
    @NotNull UUID revieweeId,
    @NotNull UUID listingId,
    @NotNull @Min(1) @Max(5) int rating,
    @Size(max = 1000) String comment) {}
