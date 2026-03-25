package com.tenga.notification.model.dto;

import com.tenga.notification.model.enums.NotificationChannel;
import com.tenga.notification.model.enums.NotificationStatus;
import com.tenga.notification.model.enums.NotificationType;
import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
    UUID id,
    NotificationType type,
    NotificationChannel channel,
    String subject,
    String message,
    NotificationStatus status,
    boolean read,
    Instant createdAt) {}
