package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.ACCESS_TOKEN_NOT_FOUND;

public class AccessTokenNotFoundException extends CustomBaseException {
    public AccessTokenNotFoundException() {
        super(ACCESS_TOKEN_NOT_FOUND.getMessage());
    }

    @Override
    public int getStatusCode() {
        return ACCESS_TOKEN_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
        return ACCESS_TOKEN_NOT_FOUND.name();
    }
}
