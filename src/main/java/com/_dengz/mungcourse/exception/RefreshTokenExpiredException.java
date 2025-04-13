package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.REFRESH_TOKEN_EXPIRED;
import static com._dengz.mungcourse.exception.GlobalErrorCode.REFRESH_TOKEN_INVALID;

public class RefreshTokenExpiredException extends CustomBaseException {
  public RefreshTokenExpiredException() {
    super(REFRESH_TOKEN_EXPIRED.getMessage());
  }

  @Override
  public int getStatusCode() {
    return REFRESH_TOKEN_EXPIRED.getStatus();
  }

  @Override
  public String getErrorCode() {
    return REFRESH_TOKEN_EXPIRED.name();
  }
}
