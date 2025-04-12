package com._dengz.mungcourse.jwt;

import com._dengz.mungcourse.dto.common.ErrorResponse;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.GlobalErrorCode;
import com._dengz.mungcourse.exception.RefreshTokenNotFoundException;
import com._dengz.mungcourse.exception.UserNotFoundException;
import com._dengz.mungcourse.properties.SecurityProperties;
import com._dengz.mungcourse.repository.RefreshTokenRepository;
import com._dengz.mungcourse.repository.UserRepository;
import com._dengz.mungcourse.util.ResponseWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import com._dengz.mungcourse.entity.RefreshToken;


import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final SecurityProperties securityProperties;
    private final AntPathMatcher pathMatcher;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        boolean result = Arrays.stream(securityProperties.getPermitUrls())
                .anyMatch(permitUrl -> pathMatcher.match(permitUrl, path));

        return result;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = tokenProvider.extractAccessToken(request).orElse(null);

        if (accessToken == null) {
            sendErrorResponse(response, GlobalErrorCode.ACCESS_TOKEN_NOT_FOUND);
            return;
        }

        if (!tokenProvider.isNotExpiredToken(accessToken)) {
            sendErrorResponse(response, GlobalErrorCode.ACCESS_TOKEN_EXPIRED);
            return;
        }

        if (!tokenProvider.isValidToken(accessToken)) {
            sendErrorResponse(response, GlobalErrorCode.ACCESS_TOKEN_INVALID); // 위조되었거나 DB에 연결되지 않은 경우
            return;
        }

    }


    private void sendErrorResponse(HttpServletResponse response, GlobalErrorCode errorCode) {
        // HTTP 상태 코드만 따로 지정 (HTTP 헤더)
        response.setStatus(errorCode.getStatus());

        // 응답 객체 생성 (HTTP 바디)
        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getMessage()
        );

        // 공통 응답 작성 유틸 사용
        ResponseWriter.writeResponse(response, errorResponse);
    }
}

//    - /login, /refresh, /logout을 제외한 모든 페이지에서 헤더에 access token 필요
//    - 토큰이 없으면 /login으로 redirect
//    - 토큰이 만료된거면 /auth/refresh로 access 토큰 재발급 요청
//    - 토큰이 위조된거면 /auth/logout로 강제 로그아웃 후 /login으로 redirect
