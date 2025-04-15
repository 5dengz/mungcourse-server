package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.WALK_NOT_FOUND;

public class WalkNotFoundException extends CustomBaseException {
    public WalkNotFoundException() {
        super(WALK_NOT_FOUND.getMessage());
    }

    @Override
    public int getStatusCode() {
        return WALK_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
        return WALK_NOT_FOUND.name();
    }
}