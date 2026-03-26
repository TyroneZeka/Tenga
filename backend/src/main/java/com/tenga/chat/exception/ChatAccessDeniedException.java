package com.tenga.chat.exception;

import com.tenga.common.exception.ForbiddenException;

public class ChatAccessDeniedException extends ForbiddenException {

  public ChatAccessDeniedException() {
    super("You are not a participant in this chat thread");
  }
}
