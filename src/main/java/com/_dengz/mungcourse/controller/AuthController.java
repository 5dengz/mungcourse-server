package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import com._dengz.mungcourse.dto.auth.IdTokenRequest;
import com._dengz.mungcourse.dto.auth.OAuth2Response;
import com._dengz.mungcourse.dto.auth.RefreshTokenRequest;
import com._dengz.mungcourse.dto.common.BaseResponse;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.exception.RefreshTokenInvalidException;
import com._dengz.mungcourse.exception.UserNotFoundException;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/refresh")
    @Operation(summary = "Access Token, Refresh Token 재발급", description = "Access Token이 만료되면 토큰들을 재발급하여 Refresh Token Rotation 구현")
    public DataResponse<AccessTokenAndRefreshTokenResponse> tokensRefresh(@RequestBody RefreshTokenRequest request) {
        return DataResponse.ok(authService.rotateTokens(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 기능", description = "클라이언트 헤더에 리프레시 토큰 있으면 DB에서 삭제 후 로그아웃, 없어도 그냥 로그아웃")
    public BaseResponse logout(HttpServletRequest request) {
        authService.logout(request);
        return DataResponse.ok("로그아웃 되었습니다.");
    }
    
}
