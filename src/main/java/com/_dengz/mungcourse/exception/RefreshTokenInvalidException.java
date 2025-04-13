package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.*;

public class RefreshTokenInvalidException extends CustomBaseException {
    public RefreshTokenInvalidException() {
        super(REFRESH_TOKEN_INVALID.getMessage());
    }

    @Override
    public int getStatusCode() {
      return REFRESH_TOKEN_INVALID.getStatus();
    }

  @Override
  public String getErrorCode() {
    return REFRESH_TOKEN_INVALID.name();
  }
}
