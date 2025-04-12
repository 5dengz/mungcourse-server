package com._dengz.mungcourse.service;

import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import com._dengz.mungcourse.exception.*;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AccessTokenAndRefreshTokenResponse rotateTokens(HttpServletRequest request) {
        String refreshToken = tokenProvider.extractRefreshToken(request)
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }

        String sub = tokenProvider.extractSub(refreshToken)
                .orElseThrow(UserNotFoundException::new);

        tokenProvider.disableRefreshToken(sub);

        return tokenProvider.createAccessAndRefreshTokenResponse(sub);
    }

    public void logout(HttpServletRequest request) {
        tokenProvider.extractRefreshToken(request)
                .ifPresent(token -> {
                    tokenProvider.extractSub(token)
                            .ifPresent(sub -> {
                                tokenProvider.disableRefreshToken(sub);
                            });
                });
    }

    public UserInfoDto getUserInfo(HttpServletRequest request) {

        String accessToken = tokenProvider.extractAccessToken(request).
                orElseThrow(AccessTokenNotFoundException::new);

        String sub = tokenProvider.extractSub(accessToken).
                orElseThrow(AccessTokenInvalidException::new);

        User user = userRepository.findBySub(sub)
                .orElseThrow(UserNotFoundException::new);

        return UserInfoDto.create(user); // DTO 변환
    }
}