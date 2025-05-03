package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.IDENTITY_TOKEN_INVALID;

public class IdentityTokenInvalidException extends CustomBaseException {
    public IdentityTokenInvalidException() {
        super(IDENTITY_TOKEN_INVALID.getMessage());
    }

    @Override
    public int getStatusCode() {
        return IDENTITY_TOKEN_INVALID.getStatus();
    }

    @Override
    public String getErrorCode() {
        return IDENTITY_TOKEN_INVALID.name();
    }
}
