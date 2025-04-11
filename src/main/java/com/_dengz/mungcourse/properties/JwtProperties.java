package com._dengz.mungcourse.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private final String issuer;
    private final String secret;
    private final Access access;
    private final Refresh refresh;

    @Getter
    @RequiredArgsConstructor
    public static class Access {
        private final int expiration;
        private final String header;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Refresh {
        private final int expiration;
        private final String header;
    }
}
