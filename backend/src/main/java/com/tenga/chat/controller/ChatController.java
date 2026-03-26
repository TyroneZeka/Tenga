package com.tenga.chat.controller;

import com.tenga.chat.model.dto.ChatMessageResponse;
import com.tenga.chat.model.dto.ChatThreadResponse;
import com.tenga.chat.model.dto.CreateThreadRequest;
import com.tenga.chat.model.dto.MessageCursorPage;
import com.tenga.chat.model.dto.SendMessageRequest;
import com.tenga.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Chat", description = "Real-time messaging between buyers and sellers")
public class ChatController {

  private final ChatService chatService;

  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @PostMapping("/threads")
  @Operation(summary = "Get or create a thread for a listing")
  public ResponseEntity<ChatThreadResponse> createThread(
      @AuthenticationPrincipal UUID userId, @Valid @RequestBody CreateThreadRequest request) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(chatService.getOrCreateThread(userId, request));
  }

  @GetMapping("/threads")
  @Operation(summary = "List all threads for the current user")
  public ResponseEntity<List<ChatThreadResponse>> getThreads(@AuthenticationPrincipal UUID userId) {
    return ResponseEntity.ok(chatService.getThreads(userId));
  }

  @PostMapping("/threads/{threadId}/messages")
  @Operation(summary = "Send a message in a thread")
  public ResponseEntity<ChatMessageResponse> sendMessage(
      @PathVariable UUID threadId,
      @AuthenticationPrincipal UUID userId,
      @Valid @RequestBody SendMessageRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(chatService.sendMessage(threadId, userId, request));
  }

  @GetMapping("/threads/{threadId}/messages")
  @Operation(summary = "Get message history (cursor-based pagination)")
  public ResponseEntity<MessageCursorPage> getMessages(
      @PathVariable UUID threadId,
      @AuthenticationPrincipal UUID userId,
      @RequestParam(required = false) UUID cursor,
      @RequestParam(defaultValue = "20") int pageSize) {
    return ResponseEntity.ok(chatService.getMessages(threadId, userId, cursor, pageSize));
  }

  @PatchMapping("/threads/{threadId}/read")
  @Operation(summary = "Mark all messages in a thread as read")
  public ResponseEntity<Void> markRead(
      @PathVariable UUID threadId, @AuthenticationPrincipal UUID userId) {
    chatService.markThreadRead(threadId, userId);
    return ResponseEntity.noContent().build();
  }
}
