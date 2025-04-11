package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

public class UserNotFoundException extends CustomBaseException {
    public UserNotFoundException() {
        super("해당 sub 값의 유저를 찾을 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
      return 404;
    }

    @Override
    public String getErrorCode() {
      return "USER_NOT_FOUND";
    }
}
