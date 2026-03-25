package com.tenga.notification.controller;

import com.tenga.notification.model.dto.NotificationResponse;
import com.tenga.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Notifications", description = "In-app notification feed")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping
  @Operation(summary = "List in-app notifications for the authenticated user")
  public ResponseEntity<Page<NotificationResponse>> getNotifications(
      @AuthenticationPrincipal UUID userId, @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(notificationService.getUserNotifications(userId, pageable));
  }

  @GetMapping("/unread-count")
  @Operation(summary = "Get unread in-app notification count")
  public ResponseEntity<Map<String, Long>> getUnreadCount(@AuthenticationPrincipal UUID userId) {
    long count = notificationService.getUnreadCount(userId);
    return ResponseEntity.ok(Map.of("unreadCount", count));
  }

  @PatchMapping("/{id}/read")
  @Operation(summary = "Mark a notification as read")
  public ResponseEntity<Void> markAsRead(
      @PathVariable UUID id, @AuthenticationPrincipal UUID userId) {
    notificationService.markAsRead(id, userId);
    return ResponseEntity.noContent().build();
  }
}
