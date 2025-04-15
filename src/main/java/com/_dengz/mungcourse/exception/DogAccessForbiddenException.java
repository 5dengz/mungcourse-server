package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.DOG_ACCESS_FORBIDDEN;

public class DogAccessForbiddenException extends CustomBaseException {
    public DogAccessForbiddenException() {
        super(DOG_ACCESS_FORBIDDEN.getMessage());
    }

    @Override
    public int getStatusCode() {
        return DOG_ACCESS_FORBIDDEN.getStatus();
    }

    @Override
    public String getErrorCode() {
        return DOG_ACCESS_FORBIDDEN.name();
    }
}