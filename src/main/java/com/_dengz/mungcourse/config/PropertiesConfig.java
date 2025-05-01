package com._dengz.mungcourse.config;

import com._dengz.mungcourse.properties.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
        JwtProperties.class,
        GoogleOAuth2Properties.class,
        AppleOAuth2Properties.class,
        SecurityProperties.class,
        AiServerProperties.class
})
public class PropertiesConfig {
}
