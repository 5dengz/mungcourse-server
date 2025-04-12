package com._dengz.mungcourse.jwt;

import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface TokenProvider {
    String createAccessToken(String sub);

    String createRefreshToken(String sub);

    void disableRefreshToken(String sub);

    Optional<String> extractAccessToken(HttpServletRequest request);

    Optional<String> extractRefreshToken(HttpServletRequest request);

    AccessTokenAndRefreshTokenResponse createAccessAndRefreshTokenResponse(String sub);

    Optional<String> extractSub(String accessToken);

    boolean isNotExpiredToken(String token);

    boolean isValidToken(String toekn);
}
