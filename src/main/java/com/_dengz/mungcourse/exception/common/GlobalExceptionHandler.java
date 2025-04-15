package com._dengz.mungcourse.exception.common;

import com._dengz.mungcourse.dto.common.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomBaseException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomBaseException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ErrorResponse.of(
                        ex.getStatusCode(),
                        ex.getErrorCode(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleParameterTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if ("date".equals(ex.getName()) && ex.getRequiredType() == LocalDate.class) {
            return ResponseEntity
                    .badRequest()
                    .body(ErrorResponse.of(
                            400,
                            "INVALID_DATE_FORMAT",
                            "날짜 형식은 YYYY-MM-DD 형식이어야 합니다."
                    ));
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(
                        400,
                        "BAD_REQUEST",
                        "잘못된 요청입니다."
                ));
    }
}
