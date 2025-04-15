package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.WALK_ACCESS_FORBIDDEN;

public class WalkAccessForbiddenException extends CustomBaseException {
    public WalkAccessForbiddenException() {
        super(WALK_ACCESS_FORBIDDEN.getMessage());
    }

    @Override
    public int getStatusCode() {
        return WALK_ACCESS_FORBIDDEN.getStatus();
    }

    @Override
    public String getErrorCode() {
        return WALK_ACCESS_FORBIDDEN.name();
    }
}