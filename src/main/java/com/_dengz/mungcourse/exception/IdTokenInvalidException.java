package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.ID_TOKEN_INVALID;

public class IdTokenInvalidException extends CustomBaseException {
    public IdTokenInvalidException() {
        super(ID_TOKEN_INVALID.getMessage());
    }

    @Override
    public int getStatusCode() {
        return ID_TOKEN_INVALID.getStatus();
    }

    @Override
    public String getErrorCode() {
        return ID_TOKEN_INVALID.name();
    }
}
