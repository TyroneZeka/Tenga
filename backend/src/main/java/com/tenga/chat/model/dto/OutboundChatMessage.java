package com.tenga.chat.model.dto;

import com.tenga.chat.model.enums.MessageType;
import java.time.Instant;
import java.util.UUID;

/** WebSocket frame pushed to subscribers after a message is persisted. */
public record OutboundChatMessage(
    UUID messageId,
    UUID threadId,
    UUID senderId,
    MessageType type,
    String body,
    String imageUrl,
    Instant sentAt) {}
