package com._dengz.mungcourse.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class DataResponse<T> extends BaseResponse {
    private final T data;

    @Builder
    protected DataResponse(boolean isSuccess, int statusCode, String message, T data) {
        super(isSuccess, statusCode, message);
        this.data = data;
    }

    public static <T> DataResponse<T> of(int statusCode, String message, T data) {
        return DataResponse.<T>builder()
                .isSuccess(true)
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> DataResponse<T> ok(T data) {
        return of(200, "요청에 성공했습니다.", data);
    }

    public static <T> DataResponse<T> ok() {
        return of(200, "요청에 성공했습니다.", null);
    }

    public static <T> DataResponse<T> ok(String message, T data) { return of(200, message, data);}

    public static <T> DataResponse<T> ok(String message) { return of(200, message, null);}

    public static <T> DataResponse<T> noContent() {
        return of(204, "콘텐츠가 없습니다.", null);
    }
}

