package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import com._dengz.mungcourse.exception.RefreshTokenInvalidException;
import com._dengz.mungcourse.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com._dengz.mungcourse.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;

    public AccessTokenAndRefreshTokenResponse rotateTokens(String refreshToken) {
        if (!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }

        String sub = tokenProvider.extractSub(refreshToken)
                .orElseThrow(UserNotFoundException::new);

        tokenProvider.disableRefreshToken(sub);

        return tokenProvider.createAccessAndRefreshTokenResponse(sub);
    }
}