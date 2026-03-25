package com.tenga.notification.service;

public interface SmsService {

  /**
   * Sends an SMS to the given phone number.
   *
   * @param phoneNumber E.164 format, e.g. +263771234567
   * @param message plain-text message body
   * @throws com.tenga.notification.exception.NotificationDeliveryException on delivery failure
   */
  void send(String phoneNumber, String message);
}
