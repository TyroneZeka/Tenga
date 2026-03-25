package com.tenga.payment.service;

import com.tenga.payment.model.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** Dev/test gateway — logs instead of calling InnBucks. */
@Service
@Qualifier("innBucksGateway")
@ConditionalOnProperty(name = "tenga.payment.innbucks.mock", havingValue = "true")
public class MockInnBucksGateway implements PaymentGateway {

  private static final Logger log = LoggerFactory.getLogger(MockInnBucksGateway.class);

  @Override
  public String initiate(Transaction transaction) {
    String ref = "MOCK-INN-" + transaction.getId();
    log.info(
        "[MOCK] InnBucks push to {} for {} {} — ref={}",
        transaction.getPayerPhone(),
        transaction.getAmount(),
        transaction.getCurrency(),
        ref);
    return ref;
  }
}
