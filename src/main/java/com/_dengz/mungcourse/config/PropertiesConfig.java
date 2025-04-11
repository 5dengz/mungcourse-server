package com._dengz.mungcourse.config;

import com._dengz.mungcourse.properties.GoogleOAuth2Properties;
import com._dengz.mungcourse.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({
        JwtProperties.class,
        GoogleOAuth2Properties.class
})
public class PropertiesConfig {
}
