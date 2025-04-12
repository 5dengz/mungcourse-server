package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.USER_NOT_FOUND;

public class UserNotFoundException extends CustomBaseException {
    public UserNotFoundException() {
        super(USER_NOT_FOUND.getMessage());
    }

    @Override
    public int getStatusCode() {
      return USER_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
      return USER_NOT_FOUND.name();
    }
}
