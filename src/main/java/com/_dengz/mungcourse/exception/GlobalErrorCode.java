package com._dengz.mungcourse.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode {
    ACCESS_TOKEN_NOT_FOUND(401, "Access Token이 존재하지 않습니다."),
    ACCESS_TOKEN_EXPIRED(401, "Access Token이 만료되었습니다."),
    ACCESS_TOKEN_INVALID(401, "Access Token이 유효하지 않습니다."),
    FILE_NAME_EXTENSION_NOT_FONUD(401, "허용 가능한 파일 이름 확장자가 아닙니다."),
    GOOGLE_ID_TOKEN_INVALID(401, "구글 ID 토큰이 유효하지 않습니다."),
    DOG_IMAGE_NOT_FOUND(401, "해당 강아지의 이미지가 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(401,"Refresh Token이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(401, "Refresh Token이 만료되었습니다."),
    REFRESH_TOKEN_INVALID(401, "Refresh Token이 유효하지 않습니다."),
    DOG_ACCESS_FORBIDDEN(403, "해당 강이지에 접근할 권한이 없습니다."),
    DOG_IMAGE_ACCESS_FORBIDDEN(403, "해당 강이지에 접근할 권한이 없습니다."),
    WALK_ACCESS_FORBIDDEN(403, "해당 산책 데이터에 접근할 권한이 없습니다."),
    GOOGLE_ID_TOKEN_NOT_FOUND(404, "구글 ID 토큰이 존재하지 않습니다."),
    USER_NOT_FOUND(404, "해당 사용자를 찾을 수 없습니다."),
    DOG_LIST_NOT_FOUND(404, "유저의 강아지가 존재하지 않습니다."),
    DOG_NOT_FOUND(404, "해당 강아지가 존재하지 않습니다."),
    MAIN_DOG_NOT_FOUND(404, "메인 강아지가 존재하지 않습니다."),
    WALK_NOT_FOUND(404, "해당 산책 데이터가 존재하지 않습니다."),
    GOOGLE_PUBLIC_KEY_NOT_FOUND(404, "kid가 일치하는 구글의 공개키를 찾을 수 없습니다."),
    GPS_SERIALIZATION_FAILED(500, "GPS 데이터를 JSON으로 변환하는데 실패했습니다."),
    GPS_DESERIALIZATION_FAILED(500, "JSON 형식을 GPS 데이터로 변환하는데 실패했습니다.");

    private final int status;
    private final String message;
}