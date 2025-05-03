package com._dengz.mungcourse.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "ai")
public class AiServerProperties {
    private final Server server;

    @Getter
    @RequiredArgsConstructor
    public static class Server {
        private final RequestUrl requestUrl;

        @Getter
        @RequiredArgsConstructor
        public static class RequestUrl {
            private final String recommend;
            private final String train;
        }
    }
}
