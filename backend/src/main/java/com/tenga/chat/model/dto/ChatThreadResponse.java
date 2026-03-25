package com.tenga.chat.model.dto;

import java.time.Instant;
import java.util.UUID;

public record ChatThreadResponse(
    UUID id,
    UUID buyerId,
    UUID sellerId,
    UUID listingId,
    String lastMessagePreview,
    int unreadCount,
    Instant updatedAt) {}
