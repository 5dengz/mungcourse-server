package com._dengz.mungcourse.dto.auth;

import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.entity.RefreshToken;
import lombok.Getter;

@Getter
public class OAuth2Response {
    private AccessTokenAndRefreshTokenResponse tokens;
    private UserInfoDto user;
//    private boolean isNewUser;

    public OAuth2Response(AccessTokenAndRefreshTokenResponse tokens, UserInfoDto user) {
        this.tokens = tokens;
        this.user = user;
    }
}
