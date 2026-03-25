package com.tenga.notification.service;

import com.tenga.common.event.OtpNotificationEvent;
import com.tenga.common.exception.ForbiddenException;
import com.tenga.common.exception.ResourceNotFoundException;
import com.tenga.notification.model.dto.NotificationResponse;
import com.tenga.notification.model.entity.Notification;
import com.tenga.notification.model.enums.NotificationChannel;
import com.tenga.notification.model.enums.NotificationType;
import com.tenga.notification.model.mapper.NotificationMapper;
import com.tenga.notification.repository.NotificationRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

  private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final SmsService smsService;
  private final EmailService emailService;

  public NotificationServiceImpl(
      NotificationRepository notificationRepository,
      NotificationMapper notificationMapper,
      SmsService smsService,
      EmailService emailService) {
    this.notificationRepository = notificationRepository;
    this.notificationMapper = notificationMapper;
    this.smsService = smsService;
    this.emailService = emailService;
  }

  @Override
  @Transactional
  public void processOtpNotification(OtpNotificationEvent event) {
    NotificationChannel channel = resolveChannel(event.recipient());
    NotificationType type = resolveType(event.purpose());
    String messageBody = buildOtpMessage(type, event.code());
    String subject = buildOtpSubject(type);

    Notification notification =
        new Notification(event.userId(), type, channel, event.recipient(), subject, messageBody);
    notificationRepository.save(notification);

    try {
      if (channel == NotificationChannel.SMS) {
        smsService.send(event.recipient(), messageBody);
      } else {
        emailService.send(event.recipient(), subject, messageBody);
      }
      notification.markSent();
      log.info(
          "OTP notification sent: type={}, channel={}, userId={}", type, channel, event.userId());
    } catch (Exception ex) {
      notification.markFailed(ex.getMessage());
      // Log but do not rethrow — failed OTP delivery must not trigger Kafka retry/DLQ spam
      log.error(
          "OTP notification delivery failed: type={}, channel={}, userId={}, reason={}",
          type,
          channel,
          event.userId(),
          ex.getMessage());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<NotificationResponse> getUserNotifications(UUID userId, Pageable pageable) {
    return notificationRepository
        .findByUserIdAndChannelOrderByCreatedAtDesc(userId, NotificationChannel.IN_APP, pageable)
        .map(notificationMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public long getUnreadCount(UUID userId) {
    return notificationRepository.countByUserIdAndChannelAndReadFalse(
        userId, NotificationChannel.IN_APP);
  }

  @Override
  @Transactional
  public void markAsRead(UUID notificationId, UUID userId) {
    Notification notification =
        notificationRepository
            .findById(notificationId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Notification not found: " + notificationId));

    if (!userId.equals(notification.getUserId())) {
      throw new ForbiddenException("You do not own this notification");
    }

    if (!notification.isRead()) {
      notification.markRead();
    }
  }

  // --- private helpers ---

  private NotificationChannel resolveChannel(String recipient) {
    return recipient.startsWith("+") ? NotificationChannel.SMS : NotificationChannel.EMAIL;
  }

  private NotificationType resolveType(String purpose) {
    return switch (purpose) {
      case "PHONE_VERIFICATION" -> NotificationType.OTP_PHONE_VERIFICATION;
      case "EMAIL_VERIFICATION" -> NotificationType.OTP_EMAIL_VERIFICATION;
      case "PASSWORD_RESET" -> NotificationType.OTP_PASSWORD_RESET;
      case "LOGIN" -> NotificationType.OTP_LOGIN;
      default -> NotificationType.GENERAL;
    };
  }

  private String buildOtpMessage(NotificationType type, String code) {
    return switch (type) {
      case OTP_PHONE_VERIFICATION ->
          "Your Tenga verification code is " + code + ". Valid for 5 minutes. Do not share it.";
      case OTP_EMAIL_VERIFICATION ->
          "Your Tenga email verification code is "
              + code
              + ". Valid for 5 minutes. Do not share it.";
      case OTP_PASSWORD_RESET ->
          "Your Tenga password reset code is "
              + code
              + ". Valid for 5 minutes. If you did not request this, ignore this message.";
      case OTP_LOGIN ->
          "Your Tenga login code is " + code + ". Valid for 5 minutes. Do not share it.";
      default -> "Your Tenga code is " + code + ". Valid for 5 minutes.";
    };
  }

  private String buildOtpSubject(NotificationType type) {
    return switch (type) {
      case OTP_PHONE_VERIFICATION, OTP_EMAIL_VERIFICATION -> "Verify your Tenga account";
      case OTP_PASSWORD_RESET -> "Reset your Tenga password";
      case OTP_LOGIN -> "Your Tenga login code";
      default -> "Your Tenga code";
    };
  }
}
