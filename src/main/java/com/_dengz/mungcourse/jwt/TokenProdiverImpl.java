package com._dengz.mungcourse.jwt;

import com._dengz.mungcourse.dto.auth.AccessTokenAndRefreshTokenResponse;
import com._dengz.mungcourse.entity.RefreshToken;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.UserNotFoundException;
import com._dengz.mungcourse.properties.JwtProperties;
import com._dengz.mungcourse.repository.RefreshTokenRepository;
import com._dengz.mungcourse.repository.UserRepository;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

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
                .claim(SUB, sub)
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
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes())
                .compact();

        return refreshTokenRepository.save(RefreshToken.createRefreshToken(user, issuedRefreshToken)).getToken();
    }

    @Override
    public AccessTokenAndRefreshTokenResponse createAccessAndRefreshTokenResponse(String sub) {
        String accessToken = createAccessToken(sub);
        String refreshToken = createRefreshToken(sub);

        return new AccessTokenAndRefreshTokenResponse(accessToken, refreshToken);
    }

    private User getUserBySub(String sub) {
        return userRepository.findBySub(sub)
                .orElseThrow(UserNotFoundException::new);
    }
}
