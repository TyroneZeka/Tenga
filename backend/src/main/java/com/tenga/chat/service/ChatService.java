package com.tenga.chat.service;

import com.tenga.chat.model.dto.ChatMessageResponse;
import com.tenga.chat.model.dto.ChatThreadResponse;
import com.tenga.chat.model.dto.CreateThreadRequest;
import com.tenga.chat.model.dto.MessageCursorPage;
import com.tenga.chat.model.dto.SendMessageRequest;
import java.util.List;
import java.util.UUID;

public interface ChatService {

  /** Get or create a thread for the given buyer/seller/listing combination. */
  ChatThreadResponse getOrCreateThread(UUID buyerId, CreateThreadRequest request);

  /** List all threads the current user participates in. */
  List<ChatThreadResponse> getThreads(UUID userId);

  /** Send a message in a thread. Caller must be a participant. */
  ChatMessageResponse sendMessage(UUID threadId, UUID senderId, SendMessageRequest request);

  /** Cursor-based message history. Pass null cursor for first page. */
  MessageCursorPage getMessages(UUID threadId, UUID userId, UUID cursor, int pageSize);

  /** Mark all unread messages in a thread as read for the given user. */
  void markThreadRead(UUID threadId, UUID userId);
}
