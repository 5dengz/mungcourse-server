package com._dengz.mungcourse.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "google")
public class GoogleOAuth2Properties {
    private final String publicKeysUrl;
    private final String clientId; // 구글에서 받은 클라이언트 ID
}
