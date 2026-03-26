package com.tenga.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tenga.chat.exception.ChatAccessDeniedException;
import com.tenga.chat.exception.ChatThreadNotFoundException;
import com.tenga.chat.model.dto.ChatMessageResponse;
import com.tenga.chat.model.dto.ChatThreadResponse;
import com.tenga.chat.model.dto.CreateThreadRequest;
import com.tenga.chat.model.dto.MessageCursorPage;
import com.tenga.chat.model.dto.SendMessageRequest;
import com.tenga.chat.model.entity.ChatMessage;
import com.tenga.chat.model.entity.ChatThread;
import com.tenga.chat.model.enums.MessageType;
import com.tenga.chat.model.mapper.ChatMapperImpl;
import com.tenga.chat.repository.ChatMessageRepository;
import com.tenga.chat.repository.ChatThreadRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

  @Mock private ChatThreadRepository threadRepository;
  @Mock private ChatMessageRepository messageRepository;
  @Mock private SimpMessagingTemplate messagingTemplate;
  @Spy private ChatMapperImpl chatMapper;

  @InjectMocks private ChatServiceImpl chatService;

  // ── getOrCreateThread ──────────────────────────────────────────────────────

  @Test
  void should_returnExistingThread_when_threadAlreadyExists() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID listingId = UUID.randomUUID();

    ChatThread existing = new ChatThread(buyerId, sellerId, listingId);
    ReflectionTestUtils.setField(existing, "id", UUID.randomUUID());

    CreateThreadRequest request = new CreateThreadRequest(listingId, sellerId);

    when(threadRepository.findByBuyerIdAndSellerIdAndListingId(buyerId, sellerId, listingId))
        .thenReturn(Optional.of(existing));

    ChatThreadResponse response = chatService.getOrCreateThread(buyerId, request);

    assertThat(response.buyerId()).isEqualTo(buyerId);
    assertThat(response.sellerId()).isEqualTo(sellerId);
    verify(threadRepository, never()).save(any());
  }

  @Test
  void should_createNewThread_when_noneExists() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID listingId = UUID.randomUUID();

    ChatThread newThread = new ChatThread(buyerId, sellerId, listingId);
    ReflectionTestUtils.setField(newThread, "id", UUID.randomUUID());

    CreateThreadRequest request = new CreateThreadRequest(listingId, sellerId);

    when(threadRepository.findByBuyerIdAndSellerIdAndListingId(buyerId, sellerId, listingId))
        .thenReturn(Optional.empty());
    when(threadRepository.save(any(ChatThread.class))).thenReturn(newThread);

    ChatThreadResponse response = chatService.getOrCreateThread(buyerId, request);

    assertThat(response.buyerId()).isEqualTo(buyerId);
    verify(threadRepository).save(any(ChatThread.class));
  }

  // ── getThreads ─────────────────────────────────────────────────────────────

  @Test
  void should_returnAllThreadsForUser() {
    UUID userId = UUID.randomUUID();
    UUID otherId = UUID.randomUUID();

    ChatThread t1 = new ChatThread(userId, otherId, UUID.randomUUID());
    ReflectionTestUtils.setField(t1, "id", UUID.randomUUID());
    ChatThread t2 = new ChatThread(otherId, userId, UUID.randomUUID());
    ReflectionTestUtils.setField(t2, "id", UUID.randomUUID());

    when(threadRepository.findByParticipant(userId)).thenReturn(List.of(t1, t2));

    List<ChatThreadResponse> responses = chatService.getThreads(userId);

    assertThat(responses).hasSize(2);
  }

  // ── sendMessage ────────────────────────────────────────────────────────────

  @Test
  void should_sendTextMessage_and_pushToParticipants() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID threadId = UUID.randomUUID();

    ChatThread thread = new ChatThread(buyerId, sellerId, UUID.randomUUID());
    ReflectionTestUtils.setField(thread, "id", threadId);

    SendMessageRequest request = new SendMessageRequest(MessageType.TEXT, "Hello there", null);

    ChatMessage savedMessage = new ChatMessage(thread, buyerId, "Hello there");
    ReflectionTestUtils.setField(savedMessage, "id", UUID.randomUUID());

    when(threadRepository.findById(threadId)).thenReturn(Optional.of(thread));
    when(messageRepository.save(any(ChatMessage.class))).thenReturn(savedMessage);
    when(threadRepository.save(any(ChatThread.class))).thenReturn(thread);

    ChatMessageResponse response = chatService.sendMessage(threadId, buyerId, request);

    assertThat(response.senderId()).isEqualTo(buyerId);
    assertThat(response.type()).isEqualTo(MessageType.TEXT);
    // Both buyer and seller receive the WebSocket push
    verify(messagingTemplate, times(2))
        .convertAndSendToUser(any(String.class), any(String.class), any());
  }

  @Test
  void should_sendImageMessage_when_typeIsImage() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID threadId = UUID.randomUUID();

    ChatThread thread = new ChatThread(buyerId, sellerId, UUID.randomUUID());
    ReflectionTestUtils.setField(thread, "id", threadId);

    SendMessageRequest request =
        new SendMessageRequest(MessageType.IMAGE, null, "https://cdn.tenga.co.zw/img/photo.jpg");

    ChatMessage savedMessage =
        new ChatMessage(thread, buyerId, "https://cdn.tenga.co.zw/img/photo.jpg", true);
    ReflectionTestUtils.setField(savedMessage, "id", UUID.randomUUID());

    when(threadRepository.findById(threadId)).thenReturn(Optional.of(thread));
    when(messageRepository.save(any(ChatMessage.class))).thenReturn(savedMessage);
    when(threadRepository.save(any(ChatThread.class))).thenReturn(thread);

    ChatMessageResponse response = chatService.sendMessage(threadId, buyerId, request);

    assertThat(response.type()).isEqualTo(MessageType.IMAGE);
  }

  @Test
  void should_throwChatThreadNotFoundException_when_sendingToNonExistentThread() {
    UUID threadId = UUID.randomUUID();
    when(threadRepository.findById(threadId)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () ->
                chatService.sendMessage(
                    threadId,
                    UUID.randomUUID(),
                    new SendMessageRequest(MessageType.TEXT, "Hi", null)))
        .isInstanceOf(ChatThreadNotFoundException.class);
  }

  @Test
  void should_throwChatAccessDeniedException_when_senderIsNotParticipant() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID outsider = UUID.randomUUID();
    UUID threadId = UUID.randomUUID();

    ChatThread thread = new ChatThread(buyerId, sellerId, UUID.randomUUID());
    ReflectionTestUtils.setField(thread, "id", threadId);

    when(threadRepository.findById(threadId)).thenReturn(Optional.of(thread));

    assertThatThrownBy(
            () ->
                chatService.sendMessage(
                    threadId, outsider, new SendMessageRequest(MessageType.TEXT, "Hi", null)))
        .isInstanceOf(ChatAccessDeniedException.class);
  }

  // ── getMessages ────────────────────────────────────────────────────────────

  @Test
  void should_returnMessages_without_cursor() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID threadId = UUID.randomUUID();

    ChatThread thread = new ChatThread(buyerId, sellerId, UUID.randomUUID());
    ReflectionTestUtils.setField(thread, "id", threadId);

    ChatMessage m1 = new ChatMessage(thread, buyerId, "First");
    ReflectionTestUtils.setField(m1, "id", UUID.randomUUID());

    when(threadRepository.findById(threadId)).thenReturn(Optional.of(thread));
    when(messageRepository.findByThreadIdLatest(any(), any(PageRequest.class)))
        .thenReturn(List.of(m1));

    MessageCursorPage page = chatService.getMessages(threadId, buyerId, null, 20);

    assertThat(page.messages()).hasSize(1);
    assertThat(page.hasMore()).isFalse();
    assertThat(page.nextCursor()).isNull();
  }

  @Test
  void should_setHasMore_when_moreMessagesThanPageSize() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID threadId = UUID.randomUUID();

    ChatThread thread = new ChatThread(buyerId, sellerId, UUID.randomUUID());
    ReflectionTestUtils.setField(thread, "id", threadId);

    // Request page size 2, return 3 (limit+1) to indicate more exist
    ChatMessage m1 = new ChatMessage(thread, buyerId, "Msg 1");
    ReflectionTestUtils.setField(m1, "id", UUID.randomUUID());
    ChatMessage m2 = new ChatMessage(thread, buyerId, "Msg 2");
    ReflectionTestUtils.setField(m2, "id", UUID.randomUUID());
    ChatMessage m3 = new ChatMessage(thread, buyerId, "Msg 3");
    ReflectionTestUtils.setField(m3, "id", UUID.randomUUID());

    when(threadRepository.findById(threadId)).thenReturn(Optional.of(thread));
    when(messageRepository.findByThreadIdLatest(any(), any(PageRequest.class)))
        .thenReturn(List.of(m1, m2, m3));

    MessageCursorPage page = chatService.getMessages(threadId, buyerId, null, 2);

    assertThat(page.hasMore()).isTrue();
    assertThat(page.messages()).hasSize(2);
    assertThat(page.nextCursor()).isNotNull();
  }

  @Test
  void should_throwChatAccessDeniedException_when_fetchingMessagesAsNonParticipant() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID outsider = UUID.randomUUID();
    UUID threadId = UUID.randomUUID();

    ChatThread thread = new ChatThread(buyerId, sellerId, UUID.randomUUID());
    ReflectionTestUtils.setField(thread, "id", threadId);

    when(threadRepository.findById(threadId)).thenReturn(Optional.of(thread));

    assertThatThrownBy(() -> chatService.getMessages(threadId, outsider, null, 20))
        .isInstanceOf(ChatAccessDeniedException.class);
  }

  // ── markThreadRead ─────────────────────────────────────────────────────────

  @Test
  void should_markUnreadMessages_as_read_for_buyer() {
    UUID buyerId = UUID.randomUUID();
    UUID sellerId = UUID.randomUUID();
    UUID threadId = UUID.randomUUID();

    ChatThread thread = new ChatThread(buyerId, sellerId, UUID.randomUUID());
    ReflectionTestUtils.setField(thread, "id", threadId);

    ChatMessage unread = new ChatMessage(thread, sellerId, "Unread msg");
    ReflectionTestUtils.setField(unread, "id", UUID.randomUUID());

    when(threadRepository.findById(threadId)).thenReturn(Optional.of(thread));
    when(messageRepository.findUnreadByThreadAndNotSender(threadId, buyerId))
        .thenReturn(List.of(unread));
    when(messageRepository.saveAll(any())).thenReturn(List.of(unread));
    when(threadRepository.save(any())).thenReturn(thread);

    chatService.markThreadRead(threadId, buyerId);

    verify(messageRepository).saveAll(any());
    verify(threadRepository).save(thread);
  }

  @Test
  void should_throwChatThreadNotFoundException_when_markingReadOnMissingThread() {
    UUID threadId = UUID.randomUUID();
    when(threadRepository.findById(threadId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> chatService.markThreadRead(threadId, UUID.randomUUID()))
        .isInstanceOf(ChatThreadNotFoundException.class);
  }
}
