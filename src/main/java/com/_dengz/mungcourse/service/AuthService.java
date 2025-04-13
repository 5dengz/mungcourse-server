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

    public AccessTokenAndRefreshTokenResponse rotateTokens(String refreshToken) {

        if (!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }

        String sub = tokenProvider.extractSub(refreshToken)
                .orElseThrow(UserNotFoundException::new);

        tokenProvider.disableRefreshToken(sub);

        return tokenProvider.createAccessAndRefreshTokenResponse(sub);
    }


    public UserInfoDto getUserInfo(User user) {
        return UserInfoDto.create(user);
    }
}