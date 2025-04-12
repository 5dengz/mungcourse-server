package com._dengz.mungcourse.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // LettuceConnectionFactory 생성
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());

        // 비밀번호 설정 (만약 비밀번호가 설정되어 있으면)
        if (redisProperties.getPassword() != null && !redisProperties.getPassword().isEmpty()) {
            connectionFactory.setPassword(redisProperties.getPassword());
        }

        return connectionFactory;
    }
}
