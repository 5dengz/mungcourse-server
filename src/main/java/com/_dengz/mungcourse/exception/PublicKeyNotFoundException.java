package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.GOOGLE_PUBLIC_KEY_NOT_FOUND;

public class PublicKeyNotFoundException extends CustomBaseException {
    public PublicKeyNotFoundException() {
      super(GOOGLE_PUBLIC_KEY_NOT_FOUND.getMessage());
    }

    @Override
    public int getStatusCode() {
      return GOOGLE_PUBLIC_KEY_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
      return GOOGLE_PUBLIC_KEY_NOT_FOUND.name();
    }
}
