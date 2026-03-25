package com.tenga.chat.model.mapper;

import com.tenga.chat.model.dto.ChatMessageResponse;
import com.tenga.chat.model.dto.ChatThreadResponse;
import com.tenga.chat.model.entity.ChatMessage;
import com.tenga.chat.model.entity.ChatThread;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

  @Mapping(target = "updatedAt", source = "thread.updatedAt")
  @Mapping(target = "unreadCount", expression = "java(unreadCountFor(thread, currentUserId))")
  ChatThreadResponse toThreadResponse(ChatThread thread, UUID currentUserId);

  @Mapping(target = "threadId", source = "message.thread.id")
  @Mapping(target = "sentAt", source = "message.createdAt")
  ChatMessageResponse toMessageResponse(ChatMessage message);

  default int unreadCountFor(ChatThread thread, UUID userId) {
    if (userId.equals(thread.getBuyerId())) return thread.getBuyerUnreadCount();
    if (userId.equals(thread.getSellerId())) return thread.getSellerUnreadCount();
    return 0;
  }
}
