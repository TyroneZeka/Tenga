package com.tenga.chat.model.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateThreadRequest(@NotNull UUID listingId, @NotNull UUID sellerId) {}
