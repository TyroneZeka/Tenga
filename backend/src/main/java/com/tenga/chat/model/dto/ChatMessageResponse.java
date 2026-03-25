package com.tenga.chat.model.dto;

import com.tenga.chat.model.enums.MessageStatus;
import com.tenga.chat.model.enums.MessageType;
import java.time.Instant;
import java.util.UUID;

public record ChatMessageResponse(
    UUID id,
    UUID threadId,
    UUID senderId,
    MessageType type,
    String body,
    String imageUrl,
    MessageStatus status,
    Instant sentAt) {}
