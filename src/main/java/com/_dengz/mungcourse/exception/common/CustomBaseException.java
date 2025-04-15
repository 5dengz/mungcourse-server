package com._dengz.mungcourse.exception.common;

public abstract class CustomBaseException extends RuntimeException {
    public CustomBaseException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
    public abstract String getErrorCode();
}

/*
 * public class [예외명] extends CustomBaseException {
 *
 *     public [예외명]() {
 *         super("사용자에게 보여줄 메시지");
 *     }
 *
 *     @Override
 *     public int getStatusCode() {
 *         return [HTTP 상태코드];
 *     }
 *
 *     @Override
 *     public String getErrorCode() {
 *         return "[에러코드 문자열]";
 *     }
 * }
 */