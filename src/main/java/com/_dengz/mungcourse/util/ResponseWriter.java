package com._dengz.mungcourse.util;

import com._dengz.mungcourse.dto.common.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseWriter {

    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "utf-8";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void writeResponse(HttpServletResponse response, BaseResponse dataResponse) {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        objectMapper.registerModule(new JavaTimeModule());

        try {
            String result = objectMapper.writeValueAsString(dataResponse);
            response.getWriter().write(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
