package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import com._dengz.mungcourse.dto.auth.IdTokenRequest;
import com._dengz.mungcourse.dto.auth.OAuth2Response;
import com._dengz.mungcourse.dto.common.BaseResponse;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.exception.UserNotFoundException;
import com._dengz.mungcourse.exception.RefreshTokenInvalidException;
import com._dengz.mungcourse.exception.RefreshTokenNotFoundException;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.service.AuthService;
import com._dengz.mungcourse.service.GoogleOAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "로그인, 인증 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final GoogleOAuth2Service googleOAuth2Service;

    @PostMapping("/google/login")
    @Operation(summary = "구글 OAuth 로그인 및 회원가입", description = "모바일에서 구글 소셜 로그인으로 회원가입 및 바로 로그인 합니다")
    public DataResponse<OAuth2Response> googleOAuthLoginOrRegister(@RequestBody IdTokenRequest request) {
        return DataResponse.ok(googleOAuth2Service.authenticate(request.getIdToken()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Access Token, Refresh Token 재발급 기능", description = "헤더에 담긴 Refresh Token을 바탕으로 Access/Refresh Token을 새로 발급합니다.")
    public DataResponse<AccessTokenAndRefreshTokenResponse> tokensRefresh(HttpServletRequest request) {
        String refreshToken = tokenProvider.extractRefreshToken(request)
                .orElseThrow(RefreshTokenNotFoundException::new);

        return DataResponse.ok(authService.rotateTokens(refreshToken));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 기능", description = "클라이언트 헤더에 리프레시 토큰 있으면 DB에서 삭제 후 로그아웃, 없어도 그냥 로그아웃")
    public BaseResponse logout(HttpServletRequest request) {
        tokenProvider.extractRefreshToken(request)
                .ifPresent(token -> {
                    tokenProvider.extractSub(token)
                            .ifPresent(sub -> {
                                tokenProvider.disableRefreshToken(sub);
                            });
                });
        return DataResponse.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/me")
    @Operation(summary = "유저 정보 가져오기 기능", description = "로그인 된 유저 정보 가져오기")
    public DataResponse<UserInfoDto> searchUser(HttpServletRequest request) {
        String sub = tokenProvider.extractSub(request)
                .orElseThrow(UserNotFoundException::new);

        return DataResponse.ok(authService.getUserInfo(sub));
    }

}
