package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.ID_TOKEN_NOT_FOUND;

public class IdTokenNotFoundException extends CustomBaseException {
    public IdTokenNotFoundException() {
        super(ID_TOKEN_NOT_FOUND.getMessage());
    }

    @Override
    public int getStatusCode() {
        return ID_TOKEN_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
        return ID_TOKEN_NOT_FOUND.name();
    }
}
