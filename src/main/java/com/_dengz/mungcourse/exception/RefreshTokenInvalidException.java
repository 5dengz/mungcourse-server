package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

public class RefreshTokenInvalidException extends CustomBaseException {
    public RefreshTokenInvalidException() {
        super(GlobalErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Override
    public int getStatusCode() {
      return GlobalErrorCode.REFRESH_TOKEN_INVALID.getStatus();
    }

  @Override
  public String getErrorCode() {
    return GlobalErrorCode.REFRESH_TOKEN_INVALID.name();
  }
}
