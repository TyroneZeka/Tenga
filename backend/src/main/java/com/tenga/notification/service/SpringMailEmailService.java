package com.tenga.notification.service;

import com.tenga.notification.exception.NotificationDeliveryException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SpringMailEmailService implements EmailService {

  private static final Logger log = LoggerFactory.getLogger(SpringMailEmailService.class);

  private final JavaMailSender mailSender;

  @Value("${tenga.notification.email.from-address:noreply@tenga.co.zw}")
  private String fromAddress;

  @Value("${tenga.notification.email.from-name:Tenga}")
  private String fromName;

  public SpringMailEmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void send(String toAddress, String subject, String body) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
      helper.setFrom(fromAddress, fromName);
      helper.setTo(toAddress);
      helper.setSubject(subject);
      helper.setText(body, false);
      mailSender.send(message);
      log.info("Email sent: to={}, subject={}", toAddress, subject);
    } catch (MessagingException | java.io.UnsupportedEncodingException ex) {
      throw new NotificationDeliveryException(
          "Failed to send email to " + toAddress + ": " + ex.getMessage(), ex);
    }
  }
}
