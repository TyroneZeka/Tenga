package com.tenga.notification.event;

import com.tenga.common.event.OtpNotificationEvent;
import com.tenga.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventConsumer {

  private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);

  private final NotificationService notificationService;

  public NotificationEventConsumer(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @KafkaListener(
      topics = "${tenga.kafka.topics.otp}",
      groupId = "tenga-notifications",
      containerFactory = "kafkaListenerContainerFactory")
  public void handleOtpEvent(OtpNotificationEvent event) {
    log.debug(
        "Received OTP notification event: purpose={}, userId={}", event.purpose(), event.userId());
    notificationService.processOtpNotification(event);
  }
}
