package com.tenga.chat.service;

import com.tenga.chat.exception.ChatAccessDeniedException;
import com.tenga.chat.exception.ChatThreadNotFoundException;
import com.tenga.chat.model.dto.ChatMessageResponse;
import com.tenga.chat.model.dto.ChatThreadResponse;
import com.tenga.chat.model.dto.CreateThreadRequest;
import com.tenga.chat.model.dto.MessageCursorPage;
import com.tenga.chat.model.dto.OutboundChatMessage;
import com.tenga.chat.model.dto.SendMessageRequest;
import com.tenga.chat.model.entity.ChatMessage;
import com.tenga.chat.model.entity.ChatThread;
import com.tenga.chat.model.mapper.ChatMapper;
import com.tenga.chat.repository.ChatMessageRepository;
import com.tenga.chat.repository.ChatThreadRepository;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatServiceImpl implements ChatService {

  private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
  private static final int MAX_PAGE_SIZE = 50;

  private final ChatThreadRepository threadRepository;
  private final ChatMessageRepository messageRepository;
  private final ChatMapper chatMapper;
  private final SimpMessagingTemplate messagingTemplate;

  public ChatServiceImpl(
      ChatThreadRepository threadRepository,
      ChatMessageRepository messageRepository,
      ChatMapper chatMapper,
      SimpMessagingTemplate messagingTemplate) {
    this.threadRepository = threadRepository;
    this.messageRepository = messageRepository;
    this.chatMapper = chatMapper;
    this.messagingTemplate = messagingTemplate;
  }

  @Override
  @Transactional
  public ChatThreadResponse getOrCreateThread(UUID buyerId, CreateThreadRequest request) {
    ChatThread thread =
        threadRepository
            .findByBuyerIdAndSellerIdAndListingId(buyerId, request.sellerId(), request.listingId())
            .orElseGet(
                () ->
                    threadRepository.save(
                        new ChatThread(buyerId, request.sellerId(), request.listingId())));
    return chatMapper.toThreadResponse(thread, buyerId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChatThreadResponse> getThreads(UUID userId) {
    return threadRepository.findByParticipant(userId).stream()
        .map(t -> chatMapper.toThreadResponse(t, userId))
        .toList();
  }

  @Override
  @Transactional
  public ChatMessageResponse sendMessage(UUID threadId, UUID senderId, SendMessageRequest request) {
    ChatThread thread =
        threadRepository
            .findById(threadId)
            .orElseThrow(() -> new ChatThreadNotFoundException(threadId));
    assertParticipant(thread, senderId);

    ChatMessage message =
        switch (request.type()) {
          case TEXT -> new ChatMessage(thread, senderId, request.body());
          case IMAGE -> new ChatMessage(thread, senderId, request.imageUrl(), true);
        };
    message = messageRepository.save(message);

    String preview =
        request.type() == com.tenga.chat.model.enums.MessageType.IMAGE ? "[Image]" : request.body();
    thread.recordMessage(preview, senderId);
    threadRepository.save(thread);

    ChatMessageResponse response = chatMapper.toMessageResponse(message);

    // Push to both participants via WebSocket
    OutboundChatMessage outbound =
        new OutboundChatMessage(
            message.getId(),
            threadId,
            senderId,
            message.getType(),
            message.getBody(),
            message.getImageUrl(),
            "SENT",
            message.getCreatedAt());
    pushToUser(thread.getBuyerId(), outbound);
    pushToUser(thread.getSellerId(), outbound);

    log.info(
        "Chat message sent: threadId={}, senderId={}, type={}", threadId, senderId, request.type());
    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public MessageCursorPage getMessages(UUID threadId, UUID userId, UUID cursor, int pageSize) {
    ChatThread thread =
        threadRepository
            .findById(threadId)
            .orElseThrow(() -> new ChatThreadNotFoundException(threadId));
    assertParticipant(thread, userId);

    int limit = Math.min(pageSize, MAX_PAGE_SIZE);
    // Fetch one extra to determine hasMore
    int fetchSize = limit + 1;
    PageRequest pageable = PageRequest.of(0, fetchSize);

    List<ChatMessage> raw =
        cursor != null
            ? messageRepository.findByThreadIdBefore(threadId, cursor, pageable)
            : messageRepository.findByThreadIdLatest(threadId, pageable);

    boolean hasMore = raw.size() > limit;
    List<ChatMessage> page = hasMore ? raw.subList(0, limit) : raw;

    UUID nextCursor = hasMore ? page.get(page.size() - 1).getId() : null;
    List<ChatMessageResponse> messages = page.stream().map(chatMapper::toMessageResponse).toList();

    return new MessageCursorPage(messages, nextCursor, hasMore);
  }

  @Override
  @Transactional
  public void markThreadRead(UUID threadId, UUID userId) {
    ChatThread thread =
        threadRepository
            .findById(threadId)
            .orElseThrow(() -> new ChatThreadNotFoundException(threadId));
    assertParticipant(thread, userId);

    List<ChatMessage> unread = messageRepository.findUnreadByThreadAndNotSender(threadId, userId);
    unread.forEach(ChatMessage::markRead);
    messageRepository.saveAll(unread);
    thread.markReadBy(userId);
    threadRepository.save(thread);
  }

  private void assertParticipant(ChatThread thread, UUID userId) {
    if (!userId.equals(thread.getBuyerId()) && !userId.equals(thread.getSellerId())) {
      throw new ChatAccessDeniedException();
    }
  }

  private void pushToUser(UUID userId, OutboundChatMessage message) {
    messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/messages", message);
  }
}
