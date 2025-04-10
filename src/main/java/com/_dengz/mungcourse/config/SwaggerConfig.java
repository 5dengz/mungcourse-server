package com._dengz.mungcourse.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * API 문서 자동 생성 설정
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Mungcourse Backend API",
                description = "강아지 산책로 추천 프로그램 멍코스의 백엔드 API 문서"
        )
)
@Configuration
public class SwaggerConfig {
}