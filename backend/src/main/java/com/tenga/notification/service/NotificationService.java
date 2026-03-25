package com.tenga.notification.service;

import com.tenga.common.event.OtpNotificationEvent;
import com.tenga.notification.model.dto.NotificationResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

  /** Processes an OTP notification event from Kafka and dispatches SMS or email. */
  void processOtpNotification(OtpNotificationEvent event);

  /** Returns a user's in-app notifications, newest first. */
  Page<NotificationResponse> getUserNotifications(UUID userId, Pageable pageable);

  /** Returns the count of unread in-app notifications for a user. */
  long getUnreadCount(UUID userId);

  /** Marks a single in-app notification as read; no-op if already read or not owned by user. */
  void markAsRead(UUID notificationId, UUID userId);
}
