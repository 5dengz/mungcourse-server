package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.GOOGLE_ID_TOKEN_NOT_FOUND;

public class GoogleIdTokenNotFoundException extends CustomBaseException {
    public GoogleIdTokenNotFoundException() {
        super(GOOGLE_ID_TOKEN_NOT_FOUND.getMessage());
    }

    @Override
    public int getStatusCode() {
        return GOOGLE_ID_TOKEN_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
        return GOOGLE_ID_TOKEN_NOT_FOUND.name();
    }
}
