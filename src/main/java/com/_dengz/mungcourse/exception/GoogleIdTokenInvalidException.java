package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.GOOGLE_ID_TOKEN_INVALID;

public class GoogleIdTokenInvalidException extends CustomBaseException {
    public GoogleIdTokenInvalidException() {
        super(GOOGLE_ID_TOKEN_INVALID.getMessage());
    }

    @Override
    public int getStatusCode() {
        return GOOGLE_ID_TOKEN_INVALID.getStatus();
    }

    @Override
    public String getErrorCode() {
        return GOOGLE_ID_TOKEN_INVALID.name();
    }
}
