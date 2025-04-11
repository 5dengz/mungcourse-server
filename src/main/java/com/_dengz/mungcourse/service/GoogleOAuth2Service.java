package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.GoogleInvalidTokenException;
import com._dengz.mungcourse.exception.PublicKeyNotFoundException;
import com._dengz.mungcourse.repository.UserRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.security.interfaces.RSAPublicKey;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service implements OAuth2Service{

    private final UserRepository userRepository;

    @Value("${google_public_keys_url}")
    private final String GOOGLE_PUBLIC_KEYS_URL;

    @Value("${google_client_id}")
    private final String GOOGLE_CLIENT_ID; // 구글에서 받은 클라이언트 ID

    @Override
    public UserInfoDto authenticate(String idToken) {
        if (!validateIdToken(idToken)) {
            throw new GoogleInvalidTokenException();
        }

        // Google에서 사용자 정보를 추출
        UserInfoDto userInfo = extractUserInfo(idToken);

        // 사용자 생성 또는 업데이트
        User user = createOrUpdateUser(userInfo);

        return new UserInfoDto(user.getSub(), user.getEmail(), user.getName(), user.getProvider(), user.getUserImgUrl());
    }

    @Override
    public boolean validateIdToken(String idToken) {
        // Google의 공개키를 사용하여 id_token 검증
        try {
            // Google의 공개키 가져오기
            Map<String, Object> publicKeys = fetchGooglePublicKeys();

            // idToken을 decode하여 header에서 kid 가져오기
            DecodedJWT decodedJWT = JWT.decode(idToken);
            String kid = decodedJWT.getHeaderClaim("kid").asString();

            // kid와 일치하는 공개키 찾기
            RSAPublicKey publicKey = findPublicKey(publicKeys, kid);

            if (publicKey == null) {
                throw new PublicKeyNotFoundException();
            }

            // 서명 검증
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey, null))
                    .withAudience(GOOGLE_CLIENT_ID) // 클라이언트 ID 매칭
                    .build();

            // idToken 검증
            verifier.verify(idToken);

            // 토큰이 유효하다면 true 반환
            return true;
        } catch (JWTVerificationException e) {
            // 예외 발생 시 false 반환
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> fetchGooglePublicKeys() {
        // Google 공개키 API에서 키 목록 가져오기
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(GOOGLE_PUBLIC_KEYS_URL, Map.class);  // Map.class로 수정
    }

    private RSAPublicKey findPublicKey(Map<String, Object> publicKeys, String kid) {
        try {
            List<Map<String, String>> keys = (List<Map<String, String>>) publicKeys.get("keys");

            for (Map<String, String> key : keys) {
                if (key.get("kid").equals(kid)) {
                    // n, e 값 가져오기 (Base64 디코딩)
                    String n = key.get("n");  // Modulus
                    String e = key.get("e");  // Exponent

                    byte[] modulusBytes = Base64.getUrlDecoder().decode(n);  // Base64 URL-safe 디코딩
                    byte[] exponentBytes = Base64.getUrlDecoder().decode(e);  // Base64 URL-safe 디코딩

                    // BigInteger로 변환
                    BigInteger modulus = new BigInteger(1, modulusBytes); // "1"은 부호 없음을 의미
                    BigInteger exponent = new BigInteger(1, exponentBytes);

                    // RSA 공개 키 생성
                    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                    return (RSAPublicKey) keyFactory.generatePublic(keySpec);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while processing RSA key", e);
        }
        return null;
    }

    @Override
    public UserInfoDto extractUserInfo(String idToken) {
        try {
            // id_token을 디코딩하여 Payload에 접근
            DecodedJWT decodedJWT = JWT.decode(idToken);

            // 사용자 정보 추출 (예: sub, email, name, picture 등)
            String sub = decodedJWT.getSubject();  // 사용자 고유 ID
            String email = decodedJWT.getClaim("email").asString();  // 이메일
            String name = decodedJWT.getClaim("name").asString();  // 이름
            String picture = decodedJWT.getClaim("picture").asString();  // 프로필 사진 URL
            String provider = "google";

            // UserInfoDto 객체 생성하여 반환
            return new UserInfoDto(sub, email, name, provider, picture);
        } catch (Exception e) {
            // 예외 처리: JWT 디코딩이나 필드 추출 실패 시 예외 처리
            throw new RuntimeException("Error extracting user info from idToken", e);
        }
    }

    @Override
    public User createOrUpdateUser(UserInfoDto userInfo) {
        // 1. userInfo에서 sub를 사용해 기존 사용자 조회
        User existingUser = userRepository.findBySub(userInfo.getSub())
                .orElse(null); // 해당 사용자가 없으면 null 반환

        if (existingUser != null) {
            // 2. 기존 사용자 정보 업데이트
            existingUser.setEmail(userInfo.getEmail());  // 이메일 업데이트
            existingUser.setName(userInfo.getName());    // 이름 업데이트
            existingUser.setUserImgUrl(userInfo.getPicture());  // 프로필 사진 업데이트
            existingUser.setProvider(userInfo.getProvider());  // 인증 제공자 업데이트

            // 3. 변경된 사용자 정보 저장 (변경 사항이 있을 때만)
            if (!existingUser.getEmail().equals(userInfo.getEmail()) ||
                    !existingUser.getName().equals(userInfo.getName()) ||
                    !existingUser.getUserImgUrl().equals(userInfo.getPicture())) {
                return userRepository.save(existingUser);
            }
            else
                return existingUser; // 바뀐거 없으면 그냥 그대로 return
        } else {
            // 4. 기존 사용자가 없으면 새 사용자 생성
            User newUser = User.create(
                    userInfo.getSub(),
                    userInfo.getEmail(),
                    userInfo.getName(),
                    userInfo.getPicture(),
                    userInfo.getProvider()
            );

            // 5. 새 사용자 저장
            return userRepository.save(newUser);
        }
    }
}
