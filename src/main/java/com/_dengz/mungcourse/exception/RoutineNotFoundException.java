package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.ROUTINE_NOT_FOUND;

public class RoutineNotFoundException extends CustomBaseException {
    public RoutineNotFoundException() {
        super(ROUTINE_NOT_FOUND.getMessage());
    }

    @Override
    public int getStatusCode() {
        return ROUTINE_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
        return ROUTINE_NOT_FOUND.name();
    }
}
