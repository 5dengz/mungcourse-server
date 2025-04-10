package com._dengz.mungcourse.exception.common;

public abstract class CustomBaseException extends RuntimeException {
    public CustomBaseException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
    public abstract String getErrorCode();
}