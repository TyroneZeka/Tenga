package com.tenga.listing.model.dto;

import com.tenga.listing.model.enums.Condition;
import com.tenga.listing.model.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateListingRequest(
    @NotBlank @Size(min = 5, max = 150) String title,
    @NotBlank @Size(min = 10, max = 5000) String description,
    @NotNull @DecimalMin("0.01") BigDecimal price,
    @NotNull Currency currency,
    @NotNull Condition condition,
    @NotNull UUID categoryId,
    boolean negotiable,
    String city,
    String suburb,
    Double latitude,
    Double longitude) {}
