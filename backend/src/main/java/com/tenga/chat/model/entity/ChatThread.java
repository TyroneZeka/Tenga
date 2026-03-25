package com.tenga.chat.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "cht_threads")
public class ChatThread extends BaseEntity {

  @Column(name = "buyer_id", nullable = false)
  private UUID buyerId;

  @Column(name = "seller_id", nullable = false)
  private UUID sellerId;

  @Column(name = "listing_id", nullable = false)
  private UUID listingId;

  @Column(name = "last_message_preview", length = 200)
  private String lastMessagePreview;

  @Column(name = "buyer_unread_count", nullable = false)
  private int buyerUnreadCount = 0;

  @Column(name = "seller_unread_count", nullable = false)
  private int sellerUnreadCount = 0;

  protected ChatThread() {}

  public ChatThread(UUID buyerId, UUID sellerId, UUID listingId) {
    this.buyerId = buyerId;
    this.sellerId = sellerId;
    this.listingId = listingId;
  }

  public UUID getBuyerId() {
    return buyerId;
  }

  public UUID getSellerId() {
    return sellerId;
  }

  public UUID getListingId() {
    return listingId;
  }

  public String getLastMessagePreview() {
    return lastMessagePreview;
  }

  public int getBuyerUnreadCount() {
    return buyerUnreadCount;
  }

  public int getSellerUnreadCount() {
    return sellerUnreadCount;
  }

  public void recordMessage(String preview, UUID senderId) {
    this.lastMessagePreview =
        preview != null && preview.length() > 200 ? preview.substring(0, 200) : preview;
    if (senderId.equals(buyerId)) {
      this.sellerUnreadCount++;
    } else {
      this.buyerUnreadCount++;
    }
  }

  public void markReadBy(UUID userId) {
    if (userId.equals(buyerId)) {
      this.buyerUnreadCount = 0;
    } else if (userId.equals(sellerId)) {
      this.sellerUnreadCount = 0;
    }
  }
}
