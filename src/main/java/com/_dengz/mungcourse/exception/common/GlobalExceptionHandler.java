package com._dengz.mungcourse.exception.common;

import com._dengz.mungcourse.dto.common.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
