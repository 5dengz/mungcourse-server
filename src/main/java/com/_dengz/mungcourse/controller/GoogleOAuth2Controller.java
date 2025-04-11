package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.dto.auth.IdTokenRequest;
import com._dengz.mungcourse.dto.auth.OAuth2Response;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.service.GoogleOAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth/google")
@RequiredArgsConstructor
public class GoogleOAuth2Controller {

    private final GoogleOAuth2Service googleOAuth2Service;

    @PostMapping("/login")
    @Operation(summary = "구글 OAuth 로그인 및 회원가입", description = "모바일에서 구글 소셜 로그인으로 회원가입하는 api")
    public DataResponse<OAuth2Response> googleOAuthLoginOrRegister(@RequestBody IdTokenRequest request) {
        OAuth2Response oAuth2Response = googleOAuth2Service.authenticate(request.getIdToken());
        return DataResponse.ok(oAuth2Response);
    }

}
