package com._dengz.mungcourse.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccessTokenAndRefreshTokenResponse {
    private final String accessToken;
    private final String refreshToken;
}