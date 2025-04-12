package com._dengz.mungcourse.dto;

import com._dengz.mungcourse.entity.User;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class UserInfoDto {
    private String sub;
    private String email;
    private String name;
    private String provider;

    @Column(nullable = true)
    private String userImgUrl;

    public UserInfoDto(String sub, String email, String name, String provider, String userImgUrl) {
        this.sub = sub;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.userImgUrl = userImgUrl;
    }

    public static UserInfoDto create(User user) {
        return new UserInfoDto(
                user.getSub(),
                user.getEmail(),
                user.getName(),
                user.getProvider(),
                user.getUserImgUrl()
        );
    }
}
