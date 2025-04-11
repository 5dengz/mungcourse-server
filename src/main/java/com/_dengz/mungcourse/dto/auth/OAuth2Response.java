package com._dengz.mungcourse.dto.auth;

import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.entity.RefreshToken;
import lombok.Getter;

@Getter
public class OAuth2Response {
    private String accessToken;
    private RefreshToken refreshToken;
    private UserInfoDto user;
    private boolean isNewUser;
}
