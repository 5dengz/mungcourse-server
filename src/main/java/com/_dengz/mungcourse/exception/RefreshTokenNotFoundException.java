package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.REFRESH_TOKEN_NOT_FOUND;

public class RefreshTokenNotFoundException extends CustomBaseException {
    public RefreshTokenNotFoundException() {
        super(REFRESH_TOKEN_NOT_FOUND.getMessage());
    }

    @Override
    public int getStatusCode() {
        return REFRESH_TOKEN_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
        return REFRESH_TOKEN_NOT_FOUND.getMessage();
    }
}
