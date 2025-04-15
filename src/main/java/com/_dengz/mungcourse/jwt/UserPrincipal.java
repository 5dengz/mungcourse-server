package com._dengz.mungcourse.jwt;

import com._dengz.mungcourse.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        return user.getSub(); // 또는 email
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인은 비밀번호 없음
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // 권한 필요 없으면 빈 리스트
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}