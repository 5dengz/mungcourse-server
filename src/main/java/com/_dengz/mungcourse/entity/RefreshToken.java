package com._dengz.mungcourse.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken {
    @Id
    private Long id;
    @Indexed
    private String token;

    public static RefreshToken createRefreshToken(User user, String token) {
        return RefreshToken.builder()
                .id(user.getId())
                .token(token)
                .build();
    }
}