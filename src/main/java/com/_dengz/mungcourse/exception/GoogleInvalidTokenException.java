package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.INVALID_GOOGLE_ID_TOKEN;

public class GoogleInvalidTokenException extends CustomBaseException {
    public GoogleInvalidTokenException() {
        super(INVALID_GOOGLE_ID_TOKEN.getMessage());
    }

    @Override
    public int getStatusCode() {
        return INVALID_GOOGLE_ID_TOKEN.getStatus();
    }

    @Override
    public String getErrorCode() {
        return INVALID_GOOGLE_ID_TOKEN.name();
    }
}
