package com.tenga.common.exception;

/** Signals a domain rule violation (HTTP 422 Unprocessable Entity). */
public class BusinessException extends RuntimeException {

  public BusinessException(String message) {
    super(message);
  }
}
