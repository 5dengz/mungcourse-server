package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

public class GoogleInvalidTokenException extends CustomBaseException {
    public GoogleInvalidTokenException() {
        super("유효하지 않은 구글 토큰입니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }

    @Override
    public String getErrorCode() {
        return "INVALID_GOOGLE_ID_TOKEN";
    }
}
