package com.tenga.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class NotificationConfig {

  @Value("${tenga.kafka.topics.otp}")
  private String otpTopic;

  /** Ensures the OTP Kafka topic exists on application startup; no-op if already present. */
  @Bean
  public NewTopic otpNotificationTopic() {
    return TopicBuilder.name(otpTopic).partitions(3).replicas(1).build();
  }
}
