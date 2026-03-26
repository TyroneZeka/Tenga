package com.tenga.chat.repository;

import com.tenga.chat.model.entity.ChatMessage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

  /** Cursor-based: return messages older than (or equal to) the cursor, newest first. */
  @Query(
      "SELECT m FROM ChatMessage m WHERE m.thread.id = :threadId AND m.id <= :cursor ORDER BY m.createdAt DESC")
  List<ChatMessage> findByThreadIdBefore(UUID threadId, UUID cursor, Pageable pageable);

  @Query("SELECT m FROM ChatMessage m WHERE m.thread.id = :threadId ORDER BY m.createdAt DESC")
  List<ChatMessage> findByThreadIdLatest(UUID threadId, Pageable pageable);

  @Query(
      "SELECT m FROM ChatMessage m WHERE m.thread.id = :threadId AND m.senderId <> :readerId AND m.status <> com.tenga.chat.model.enums.MessageStatus.READ")
  List<ChatMessage> findUnreadByThreadAndNotSender(UUID threadId, UUID readerId);
}
