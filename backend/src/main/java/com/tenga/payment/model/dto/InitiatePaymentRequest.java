package com.tenga.payment.model.dto;

import com.tenga.listing.model.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.UUID;

public record InitiatePaymentRequest(
    @NotNull UUID listingId,
    @NotNull UUID sellerId,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotNull Currency currency,
    @NotBlank @Pattern(regexp = "^\\+263[0-9]{9}$") String payerPhone) {}
