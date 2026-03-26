package com.tenga.chat.model.entity;

import com.tenga.chat.model.enums.MessageStatus;
import com.tenga.chat.model.enums.MessageType;
import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cht_messages")
public class ChatMessage extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "thread_id", nullable = false)
  private ChatThread thread;

  @Column(name = "sender_id", nullable = false)
  private UUID senderId;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 10)
  private MessageType type;

  @Column(name = "body", length = 2000)
  private String body;

  @Column(name = "image_url")
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 10)
  private MessageStatus status = MessageStatus.SENT;

  @Column(name = "delivered_at")
  private Instant deliveredAt;

  @Column(name = "read_at")
  private Instant readAt;

  protected ChatMessage() {}

  public ChatMessage(ChatThread thread, UUID senderId, String body) {
    this.thread = thread;
    this.senderId = senderId;
    this.type = MessageType.TEXT;
    this.body = body;
  }

  public ChatMessage(ChatThread thread, UUID senderId, String imageUrl, boolean isImage) {
    this.thread = thread;
    this.senderId = senderId;
    this.type = MessageType.IMAGE;
    this.imageUrl = imageUrl;
  }

  public ChatThread getThread() {
    return thread;
  }

  public UUID getSenderId() {
    return senderId;
  }

  public MessageType getType() {
    return type;
  }

  public String getBody() {
    return body;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public MessageStatus getStatus() {
    return status;
  }

  public Instant getDeliveredAt() {
    return deliveredAt;
  }

  public Instant getReadAt() {
    return readAt;
  }

  public void markDelivered() {
    if (this.status == MessageStatus.SENT) {
      this.status = MessageStatus.DELIVERED;
      this.deliveredAt = Instant.now();
    }
  }

  public void markRead() {
    this.status = MessageStatus.READ;
    this.readAt = Instant.now();
    if (this.deliveredAt == null) {
      this.deliveredAt = this.readAt;
    }
  }
}
