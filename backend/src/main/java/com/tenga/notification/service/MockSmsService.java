package com.tenga.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** SMS service for local development — logs the message instead of making API calls. */
@Service
@ConditionalOnProperty(name = "tenga.notification.sms-provider", havingValue = "mock")
public class MockSmsService implements SmsService {

  private static final Logger log = LoggerFactory.getLogger(MockSmsService.class);

  @Override
  public void send(String phoneNumber, String message) {
    log.info("[MOCK SMS] to={} | message={}", phoneNumber, message);
  }
}
