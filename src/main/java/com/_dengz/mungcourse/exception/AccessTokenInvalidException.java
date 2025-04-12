package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.ACCESS_TOKEN_INVALID;

public class AccessTokenInvalidException extends CustomBaseException {
    public AccessTokenInvalidException() {
        super(ACCESS_TOKEN_INVALID.getMessage());
    }

    @Override
    public int getStatusCode() {
        return ACCESS_TOKEN_INVALID.getStatus();
    }

    @Override
    public String getErrorCode() {
        return ACCESS_TOKEN_INVALID.name();
    }
}
