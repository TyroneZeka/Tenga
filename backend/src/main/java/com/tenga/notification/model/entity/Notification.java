package com.tenga.notification.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import com.tenga.notification.model.enums.NotificationChannel;
import com.tenga.notification.model.enums.NotificationStatus;
import com.tenga.notification.model.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ntf_notifications")
public class Notification extends BaseEntity {

  @Column(name = "user_id")
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private NotificationType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private NotificationChannel channel;

  @Column(nullable = false, length = 320)
  private String recipient;

  @Column(length = 200)
  private String subject;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String message;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private NotificationStatus status;

  @Column(columnDefinition = "TEXT")
  private String errorMessage;

  @Column private Instant sentAt;

  @Column(name = "is_read", nullable = false)
  private boolean read;

  @Column private Instant readAt;

  protected Notification() {}

  public Notification(
      UUID userId,
      NotificationType type,
      NotificationChannel channel,
      String recipient,
      String subject,
      String message) {
    this.userId = userId;
    this.type = type;
    this.channel = channel;
    this.recipient = recipient;
    this.subject = subject;
    this.message = message;
    this.status = NotificationStatus.PENDING;
    this.read = false;
  }

  public void markSent() {
    this.status = NotificationStatus.SENT;
    this.sentAt = Instant.now();
  }

  public void markFailed(String reason) {
    this.status = NotificationStatus.FAILED;
    this.errorMessage = reason;
  }

  public void markRead() {
    this.read = true;
    this.readAt = Instant.now();
  }

  public UUID getUserId() {
    return userId;
  }

  public NotificationType getType() {
    return type;
  }

  public NotificationChannel getChannel() {
    return channel;
  }

  public String getRecipient() {
    return recipient;
  }

  public String getSubject() {
    return subject;
  }

  public String getMessage() {
    return message;
  }

  public NotificationStatus getStatus() {
    return status;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public Instant getSentAt() {
    return sentAt;
  }

  public boolean isRead() {
    return read;
  }

  public Instant getReadAt() {
    return readAt;
  }
}
