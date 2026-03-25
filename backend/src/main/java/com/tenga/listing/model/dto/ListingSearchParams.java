package com.tenga.listing.model.dto;

import com.tenga.listing.model.enums.Condition;
import com.tenga.listing.model.enums.Currency;
import java.math.BigDecimal;
import java.util.UUID;

public record ListingSearchParams(
    String query,
    UUID categoryId,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    Currency currency,
    Condition condition,
    String city,
    Double latitude,
    Double longitude,
    Integer radiusKm) {}
