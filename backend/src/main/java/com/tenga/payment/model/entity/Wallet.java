package com.tenga.payment.model.entity;

import com.tenga.common.model.entity.BaseEntity;
import com.tenga.listing.model.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pay_wallets")
public class Wallet extends BaseEntity {

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", nullable = false, length = 10)
  private Currency currency;

  @Column(name = "balance", nullable = false, precision = 14, scale = 2)
  private BigDecimal balance = BigDecimal.ZERO;

  protected Wallet() {}

  public Wallet(UUID userId, Currency currency) {
    this.userId = userId;
    this.currency = currency;
  }

  public UUID getUserId() {
    return userId;
  }

  public Currency getCurrency() {
    return currency;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void credit(BigDecimal amount) {
    this.balance = this.balance.add(amount);
  }

  public void debit(BigDecimal amount) {
    if (this.balance.compareTo(amount) < 0) {
      throw new IllegalStateException("Insufficient wallet balance");
    }
    this.balance = this.balance.subtract(amount);
  }
}
