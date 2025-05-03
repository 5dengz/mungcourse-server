package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.NONCE_INVALID;

public class NonceInvalidException extends CustomBaseException {
  public NonceInvalidException() {
    super(NONCE_INVALID.getMessage());
  }

  @Override
  public int getStatusCode() {
    return NONCE_INVALID.getStatus();
  }

  @Override
  public String getErrorCode() {
    return NONCE_INVALID.name();
  }
}
