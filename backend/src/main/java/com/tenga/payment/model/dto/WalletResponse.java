package com.tenga.payment.model.dto;

import com.tenga.listing.model.enums.Currency;
import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(UUID userId, Currency currency, BigDecimal balance) {}
