package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.IDENTITY_TOKEN_NOT_FOUND;

public class IdentityTokenNotFoundException extends CustomBaseException {
  public IdentityTokenNotFoundException() {
    super(IDENTITY_TOKEN_NOT_FOUND.getMessage());
  }

  @Override
  public int getStatusCode() {
    return IDENTITY_TOKEN_NOT_FOUND.getStatus();
  }

  @Override
  public String getErrorCode() {
    return IDENTITY_TOKEN_NOT_FOUND.name();
  }
}