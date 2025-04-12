package com._dengz.mungcourse.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode {
    ACCESS_TOKEN_NOT_FOUND(401, "Access Token이 존재하지 않습니다."),
    ACCESS_TOKEN_EXPIRED(401, "Access Token이 만료되었습니다."),
    ACCESS_TOKEN_INVALID(401, "Access Token이 유효하지 않습니다."),
    REFRESH_TOKEN_INVALID(401, "Refresh Token이 유효하지 않습니다."),
    USER_NOT_FOUND(404, "해당 사용자를 찾을 수 없습니다."),
    INVALID_GOOGLE_ID_TOKEN(401, "유효하지 않은 구글 토큰입니다."),
    GOOGLE_PUBLIC_KEY_NOT_FOUND(404, "kid가 일치하는 구글의 공개키를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(404,"해당 리프레시 토큰을 찾을 수 없습니다.");

    private final int status;
    private final String message;
}