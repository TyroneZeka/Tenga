package com.tenga.payment.service;

import com.tenga.payment.model.entity.Transaction;

/**
 * Abstraction over mobile-money payment gateways. Implementations call the external API and return
 * a gateway reference on success.
 */
public interface PaymentGateway {

  /** Initiates a payment push. Returns the gateway-assigned reference. */
  String initiate(Transaction transaction);
}
