package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.dto.auth.OAuth2Response;
import com._dengz.mungcourse.entity.User;

public interface OAuth2Service {
    // 소셜 로그인 인증 후 로그인 및 회원가입 후 로그인
    OAuth2Response authenticate(String idToken);

    // idToken의 유효성 검증
    boolean validateIdToken(String idToken);

    // idToken에서 사용자 정보 추출
    UserInfoDto extractUserInfo(String idToken);

    // 사용자 정보 생성 또는 업데이트
    User createUser(UserInfoDto userInfo);

    User updateUser(String sub, UserInfoDto userInfo);

}
