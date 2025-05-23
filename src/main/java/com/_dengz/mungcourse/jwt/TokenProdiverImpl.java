package com._dengz.mungcourse.jwt;

import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import com._dengz.mungcourse.entity.RefreshToken;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.UserNotFoundException;
import com._dengz.mungcourse.properties.JwtProperties;
import com._dengz.mungcourse.repository.RefreshTokenRepository;
import com._dengz.mungcourse.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com._dengz.mungcourse.jwt.JwtConstants.*;

@Service
@RequiredArgsConstructor
public class TokenProdiverImpl implements TokenProvider{

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public String createAccessToken(String sub) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getAccess().getExpiration());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setIssuer(jwtProperties.getIssuer())
                .claim(USER_SUB, sub)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes())
                .compact();
    }

    @Override
    public String createRefreshToken(String sub) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getRefresh().getExpiration());

        User user = getUserBySub(sub);

        String issuedRefreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setIssuer(jwtProperties.getIssuer())
                .claim(USER_SUB, sub)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes())
                .compact();

        return refreshTokenRepository.save(RefreshToken.createRefreshToken(user, issuedRefreshToken)).getToken();
    }

    @Override
    public void disableRefreshToken(String sub) {
        userRepository.findBySub(sub)
                .map(User::getId)
                .ifPresent(refreshTokenRepository::deleteById);
    }

    @Override
    public AccessTokenAndRefreshTokenResponse createAccessAndRefreshTokenResponse(String sub) {
        String accessToken = createAccessToken(sub);
        String refreshToken = createRefreshToken(sub);

        return new AccessTokenAndRefreshTokenResponse(accessToken, refreshToken);
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getAccess().getHeader()))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getRefresh().getHeader()))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    @Override
    public Optional<String> extractSub(String token) {
        return getClaims(token)
                .map(claims -> claims.get(USER_SUB, String.class));
    }

    @Override
    public Optional<String> extractSub(HttpServletRequest request) {
        return extractAccessToken(request).flatMap(this::extractSub);

    }

    @Override
    public boolean isNotExpiredToken(String token) {
        return getClaims(token)
                .map(claims -> claims.getExpiration().after(new Date()))
                .orElse(false); // 파싱 실패 시 false 반환
    }

    @Override
    public boolean isValidAccessToken(String token) {
        try {
            if (!isAccessToken(token)) return false;

            String sub = extractSub(token).orElseThrow();

            return userRepository.findBySub(sub)
                    .map(User::getId)
                    .flatMap(refreshTokenRepository::findById)
                    .isPresent(); // RefreshToken이 존재해야 AccessToken도 유효
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isValidRefreshToken(String token) {
        try {
            if (!isRefreshToken(token)) return false;

            return refreshTokenRepository.findByToken(token).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    private Optional<Claims> getClaims(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private boolean isAccessToken(String token) {
        return getClaims(token)
                .map(claims -> ACCESS_TOKEN_SUBJECT.equals(claims.getSubject()))
                .orElse(false); // 파싱 실패 = 위조 or 변조 → false
    }

    private boolean isRefreshToken(String token) {
        return getClaims(token)
                .map(claims -> REFRESH_TOKEN_SUBJECT.equals(claims.getSubject()))
                .orElse(false); // 파싱 실패 = 위조 or 변조 → false
    }

    private User getUserBySub(String sub) {
        return userRepository.findBySub(sub)
                .orElseThrow(UserNotFoundException::new);
    }
}
