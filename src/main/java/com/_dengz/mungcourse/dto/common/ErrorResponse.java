package com._dengz.mungcourse.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends BaseResponse {

    private final String error;

    @Builder
    protected ErrorResponse(int statusCode, String error, String message) {
        super(false, statusCode, message);
        this.error = error;
    }

    public static ErrorResponse of(int statusCode, String error, String message) {
        return ErrorResponse.builder()
                .statusCode(statusCode)
                .error(error)
                .message(message)
                .build();
    }
}