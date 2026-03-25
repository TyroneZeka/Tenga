package com.tenga.payment.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import com.tenga.listing.model.enums.Currency;
import com.tenga.payment.model.enums.PaymentMethod;
import com.tenga.payment.model.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "pay_transactions")
public class Transaction extends BaseEntity {

  @Column(name = "buyer_id", nullable = false)
  private UUID buyerId;

  @Column(name = "seller_id", nullable = false)
  private UUID sellerId;

  @Column(name = "listing_id", nullable = false)
  private UUID listingId;

  @Column(name = "amount", nullable = false, precision = 14, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", nullable = false, length = 10)
  private Currency currency;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method", nullable = false, length = 10)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 15)
  private TransactionStatus status = TransactionStatus.PENDING;

  /** Phone number used for mobile-money payment (e.g. +263771234567). */
  @Column(name = "payer_phone", length = 15)
  private String payerPhone;

  /** Gateway-assigned reference returned when initiating the payment. */
  @Column(name = "gateway_reference", unique = true)
  private String gatewayReference;

  /** Human-readable failure reason, set when status = FAILED. */
  @Column(name = "failure_reason")
  private String failureReason;

  @Column(name = "completed_at")
  private Instant completedAt;

  @Column(name = "refunded_at")
  private Instant refundedAt;

  protected Transaction() {}

  public Transaction(
      UUID buyerId,
      UUID sellerId,
      UUID listingId,
      BigDecimal amount,
      Currency currency,
      PaymentMethod paymentMethod,
      String payerPhone) {
    this.buyerId = buyerId;
    this.sellerId = sellerId;
    this.listingId = listingId;
    this.amount = amount;
    this.currency = currency;
    this.paymentMethod = paymentMethod;
    this.payerPhone = payerPhone;
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

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public String getPayerPhone() {
    return payerPhone;
  }

  public String getGatewayReference() {
    return gatewayReference;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public Instant getCompletedAt() {
    return completedAt;
  }

  public Instant getRefundedAt() {
    return refundedAt;
  }

  public void markProcessing(String gatewayReference) {
    this.status = TransactionStatus.PROCESSING;
    this.gatewayReference = gatewayReference;
  }

  public void markCompleted() {
    this.status = TransactionStatus.COMPLETED;
    this.completedAt = Instant.now();
  }

  public void markFailed(String reason) {
    this.status = TransactionStatus.FAILED;
    this.failureReason = reason;
  }

  public void markRefunded() {
    this.status = TransactionStatus.REFUNDED;
    this.refundedAt = Instant.now();
  }
}
