package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

public class PublicKeyNotFoundException extends CustomBaseException {
    public PublicKeyNotFoundException() {
      super("kid가 일치하는 구글의 공개키를 찾을 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
      return 404;
    }

    @Override
    public String getErrorCode() {
      return "GOOGLE_PUBLIC_KEY_NOT_FOUND";
    }
}
