package com.tenga.payment.exception;

import com.tenga.common.exception.ResourceNotFoundException;
import java.util.UUID;

public class PaymentNotFoundException extends ResourceNotFoundException {

  public PaymentNotFoundException(UUID id) {
    super("Transaction", id);
  }
}
