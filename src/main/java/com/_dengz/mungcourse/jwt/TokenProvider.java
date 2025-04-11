package com._dengz.mungcourse.jwt;

import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;

public interface TokenProvider {
    String createAccessToken(String sub);

    String createRefreshToken(String studentNumber);

    public AccessTokenAndRefreshTokenResponse createAccessAndRefreshTokenResponse(String sub);
}
