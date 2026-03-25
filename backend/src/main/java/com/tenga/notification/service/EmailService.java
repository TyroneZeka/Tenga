package com.tenga.notification.service;

public interface EmailService {

  /**
   * Sends a plain-text email.
   *
   * @param toAddress recipient email address
   * @param subject email subject line
   * @param body plain-text message body
   * @throws com.tenga.notification.exception.NotificationDeliveryException on delivery failure
   */
  void send(String toAddress, String subject, String body);
}
