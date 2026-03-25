package com.tenga.notification.repository;

import com.tenga.notification.model.entity.Notification;
import com.tenga.notification.model.enums.NotificationChannel;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  Page<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

  Page<Notification> findByUserIdAndChannelOrderByCreatedAtDesc(
      UUID userId, NotificationChannel channel, Pageable pageable);

  long countByUserIdAndChannelAndReadFalse(UUID userId, NotificationChannel channel);
}
