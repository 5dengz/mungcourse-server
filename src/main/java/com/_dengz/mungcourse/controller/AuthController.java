package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import com._dengz.mungcourse.dto.auth.IdTokenRequest;
import com._dengz.mungcourse.dto.auth.OAuth2Response;
import com._dengz.mungcourse.dto.auth.RefreshTokenRequest;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.exception.RefreshTokenInvalidException;
import com._dengz.mungcourse.exception.UserNotFoundException;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    @Operation(summary = "Access Token, Refresh Token 재발급", description = "Access Token이 만료되면 토큰들을 재발급하여 Refresh Token Rotation 구현")
    public DataResponse<AccessTokenAndRefreshTokenResponse> tokensRefresh(@RequestBody RefreshTokenRequest request) {
        return DataResponse.ok(authService.rotateTokens(request.getRefreshToken()));
    }
}
