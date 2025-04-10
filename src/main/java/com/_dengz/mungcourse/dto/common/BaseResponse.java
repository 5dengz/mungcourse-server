package com._dengz.mungcourse.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(example = "2024-01-00 13:00:00", type = "string")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final boolean isSuccess;
    @Schema(example = "200")
    private final int statusCode;
    @Schema(example = "요청에 성공했습니다.")
    private final String message;

    protected BaseResponse(boolean isSuccess, int statusCode, String message) {
        this.isSuccess = isSuccess;
        this.statusCode = statusCode;
        this.message = message;
    }

}
