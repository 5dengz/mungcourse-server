package com._dengz.mungcourse.service;

import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import com._dengz.mungcourse.exception.*;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AccessTokenAndRefreshTokenResponse rotateTokens(String refreshToken) {

        if (refreshToken == null)
            throw new RefreshTokenNotFoundException();

        else if (!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }

        else if (!tokenProvider.isNotExpiredToken(refreshToken)) {
            throw new RefreshTokenExpiredException();
        }

        String sub = tokenProvider.extractSub(refreshToken)
                .orElseThrow(UserNotFoundException::new);

        tokenProvider.disableRefreshToken(sub);

        return tokenProvider.createAccessAndRefreshTokenResponse(sub);
    }


    public UserInfoDto getUserInfo(User user) {
        return UserInfoDto.create(user);
    }

    public void logoutUser(String refreshToken) {
        if (refreshToken != null) { // null이면 아무것도 실행되지 않음
            tokenProvider.extractSub(refreshToken)
                    .ifPresent(tokenProvider::disableRefreshToken);
        }
    }

    @Transactional
    public void deleteUser(String sub, String refreshToken) {
        userRepository.deleteBySub(sub);
        logoutUser(refreshToken);
    }
}