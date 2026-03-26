package com.tenga.chat.model.dto;

import java.util.List;
import java.util.UUID;

public record MessageCursorPage(
    List<ChatMessageResponse> messages, UUID nextCursor, boolean hasMore) {}
