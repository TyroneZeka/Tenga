package com.tenga.chat.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
    @NotNull com.tenga.chat.model.enums.MessageType type,
    @Size(max = 2000) String body,
    String imageUrl) {}
