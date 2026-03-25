package com.tenga.listing.model.dto;

import com.tenga.listing.model.enums.Condition;
import com.tenga.listing.model.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateListingRequest(
    @NotBlank(message = "Title is required")
        @Size(min = 5, max = 150, message = "Title must be between 5 and 150 characters")
        String title,
    @NotBlank(message = "Description is required")
        @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
        String description,
    @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal price,
    @NotNull(message = "Currency is required") Currency currency,
    @NotNull(message = "Condition is required") Condition condition,
    @NotNull(message = "Category is required") UUID categoryId,
    boolean negotiable,
    String city,
    String suburb,
    Double latitude,
    Double longitude) {}
