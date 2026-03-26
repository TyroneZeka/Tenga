package com.tenga.chat.model.mapper;

import com.tenga.chat.model.dto.ChatMessageResponse;
import com.tenga.chat.model.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

  @Mapping(target = "threadId", source = "message.thread.id")
  @Mapping(target = "sentAt", source = "message.createdAt")
  ChatMessageResponse toMessageResponse(ChatMessage message);
}
