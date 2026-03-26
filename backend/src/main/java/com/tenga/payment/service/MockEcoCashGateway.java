package com.tenga.payment.service;

import com.tenga.payment.model.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Dev/test gateway — logs instead of calling EcoCash. */
@Service
@Qualifier("ecoCashGateway")
@ConditionalOnProperty(name = "tenga.payment.ecocash.mock", havingValue = "true")
public class MockEcoCashGateway implements PaymentGateway {

  private static final Logger log = LoggerFactory.getLogger(MockEcoCashGateway.class);

  @Override
  public String initiate(Transaction transaction) {
    String ref = "MOCK-ECO-" + transaction.getId();
    log.info(
        "[MOCK] EcoCash USSD push to {} for ZWL {} — ref={}",
        transaction.getPayerPhone(),
        transaction.getAmount(),
        ref);
    return ref;
  }
}
