package com.tenga.chat.exception;

import com.tenga.common.exception.ResourceNotFoundException;
import java.util.UUID;

public class ChatThreadNotFoundException extends ResourceNotFoundException {

  public ChatThreadNotFoundException(UUID threadId) {
    super("Chat thread not found: " + threadId);
  }
}
