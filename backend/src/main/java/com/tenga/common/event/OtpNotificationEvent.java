package com.tenga.common.event;

import java.util.UUID;

/**
 * Kafka event published by the auth module when an OTP needs to be delivered. Consumed by the
 * notification module for SMS/email dispatch.
 */
public record OtpNotificationEvent(UUID userId, String recipient, String code, String purpose) {}
