package com.tenga.payment.model.dto;

import com.tenga.listing.model.enums.Currency;
import com.tenga.payment.model.enums.PaymentMethod;
import com.tenga.payment.model.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
    UUID id,
    UUID listingId,
    UUID buyerId,
    UUID sellerId,
    BigDecimal amount,
    Currency currency,
    PaymentMethod paymentMethod,
    TransactionStatus status,
    String gatewayReference,
    Instant createdAt,
    Instant completedAt) {}
