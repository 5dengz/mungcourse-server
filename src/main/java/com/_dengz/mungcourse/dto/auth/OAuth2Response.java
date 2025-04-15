package com._dengz.mungcourse.dto.auth;

import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.entity.RefreshToken;
import lombok.Getter;

@Getter
public class OAuth2Response {
    private AccessTokenAndRefreshTokenResponse tokens;
    private UserInfoDto user;
    private Boolean isNewUser;

    public OAuth2Response(AccessTokenAndRefreshTokenResponse tokens, UserInfoDto user, boolean isNewUser) {
        this.tokens = tokens;
        this.user = user;
        this.isNewUser = isNewUser;
    }
}
