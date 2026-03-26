package com.tenga.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tenga.common.event.OtpNotificationEvent;
import com.tenga.common.exception.ForbiddenException;
import com.tenga.common.exception.ResourceNotFoundException;
import com.tenga.notification.model.entity.Notification;
import com.tenga.notification.model.enums.NotificationChannel;
import com.tenga.notification.model.enums.NotificationStatus;
import com.tenga.notification.model.mapper.NotificationMapper;
import com.tenga.notification.repository.NotificationRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

  @Mock private NotificationRepository notificationRepository;
  @Mock private NotificationMapper notificationMapper;
  @Mock private SmsService smsService;
  @Mock private EmailService emailService;

  @InjectMocks private NotificationServiceImpl notificationService;

  @Test
  void should_routeToSms_when_recipientStartsWithPlus() throws Exception {
    UUID userId = UUID.randomUUID();
    OtpNotificationEvent event =
        new OtpNotificationEvent(userId, "+263771234567", "123456", "PHONE_VERIFICATION");

    when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    notificationService.processOtpNotification(event);

    verify(smsService).send(eq("+263771234567"), any());
    verify(emailService, never()).send(any(), any(), any());
  }

  @Test
  void should_routeToEmail_when_recipientIsEmailAddress() throws Exception {
    UUID userId = UUID.randomUUID();
    OtpNotificationEvent event =
        new OtpNotificationEvent(userId, "buyer@example.com", "654321", "EMAIL_VERIFICATION");

    when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    notificationService.processOtpNotification(event);

    verify(emailService).send(eq("buyer@example.com"), any(), any());
    verify(smsService, never()).send(any(), any());
  }

  @Test
  void should_markNotificationSent_when_deliverySucceeds() {
    UUID userId = UUID.randomUUID();
    OtpNotificationEvent event =
        new OtpNotificationEvent(userId, "+263771234567", "999999", "LOGIN");

    ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
    when(notificationRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

    notificationService.processOtpNotification(event);

    // After markSent() the status should be SENT
    Notification saved = captor.getValue();
    assertThat(saved.getStatus()).isEqualTo(NotificationStatus.SENT);
    assertThat(saved.getChannel()).isEqualTo(NotificationChannel.SMS);
  }

  @Test
  void should_markNotificationFailed_when_smsThrows() throws Exception {
    UUID userId = UUID.randomUUID();
    OtpNotificationEvent event =
        new OtpNotificationEvent(userId, "+263771234567", "000000", "LOGIN");

    ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
    when(notificationRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));
    doThrow(new RuntimeException("SMS gateway timeout")).when(smsService).send(any(), any());

    // Should NOT throw — failures are swallowed to prevent Kafka retry spam
    notificationService.processOtpNotification(event);

    Notification saved = captor.getValue();
    assertThat(saved.getStatus()).isEqualTo(NotificationStatus.FAILED);
    assertThat(saved.getErrorMessage()).contains("SMS gateway timeout");
  }

  @Test
  void should_markNotificationFailed_when_emailThrows() throws Exception {
    UUID userId = UUID.randomUUID();
    OtpNotificationEvent event =
        new OtpNotificationEvent(userId, "user@example.com", "111111", "PASSWORD_RESET");

    ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
    when(notificationRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));
    doThrow(new RuntimeException("SMTP connection refused")).when(emailService).send(any(), any(), any());

    notificationService.processOtpNotification(event);

    Notification saved = captor.getValue();
    assertThat(saved.getStatus()).isEqualTo(NotificationStatus.FAILED);
  }

  @Test
  void should_throwForbiddenException_when_markingAnotherUsersNotificationRead() {
    UUID ownerId = UUID.randomUUID();
    UUID otherId = UUID.randomUUID();
    UUID notificationId = UUID.randomUUID();

    Notification notification =
        new Notification(ownerId, null, NotificationChannel.IN_APP, "user@example.com", "Subject", "Body");
    ReflectionTestUtils.setField(notification, "id", notificationId);

    when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

    assertThatThrownBy(() -> notificationService.markAsRead(notificationId, otherId))
        .isInstanceOf(ForbiddenException.class);
  }

  @Test
  void should_throwResourceNotFoundException_when_notificationDoesNotExist() {
    UUID notificationId = UUID.randomUUID();
    when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> notificationService.markAsRead(notificationId, UUID.randomUUID()))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
