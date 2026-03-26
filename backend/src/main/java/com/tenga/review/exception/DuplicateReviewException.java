package com.tenga.review.exception;

import com.tenga.common.exception.ConflictException;

public class DuplicateReviewException extends ConflictException {

  public DuplicateReviewException() {
    super("A review for this transaction already exists");
  }
}
