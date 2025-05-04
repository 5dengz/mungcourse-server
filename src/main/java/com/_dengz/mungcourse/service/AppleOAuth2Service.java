package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.UserInfoDto;
import com._dengz.mungcourse.dto.auth.OAuth2Response;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.*;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.properties.AppleOAuth2Properties;
import com._dengz.mungcourse.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleOAuth2Service {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AppleOAuth2Properties appleOAuth2Properties;

    // 소셜 로그인 인증 후 로그인 및 회원가입 후 로그인
    public OAuth2Response authenticate(String identityToken, String nonce) {
        if (!validateIdToken(identityToken, nonce)) {
            throw new IdentityTokenInvalidException();
        }

        UserInfoDto userInfo = extractUserInfo(identityToken);
        boolean isNewUser = false;
        User user;

        if (!isUserExist(userInfo.getSub())) {
            user = createUser(userInfo);
            isNewUser = true;
        } else {
            user = updateUser(userInfo.getSub(), userInfo);
        }

        return new OAuth2Response(
                tokenProvider.createAccessAndRefreshTokenResponse(user.getSub()),
                UserInfoDto.create(user),
                isNewUser
        );
    }


    public boolean validateIdToken(String identityToken, String clientNonce) {

        if (identityToken.isEmpty())
            throw new IdentityTokenNotFoundException();

        // Google의 공개키를 사용하여 id_token 검증
        try {
            // Google의 공개키 가져오기
            Map<String, Object> publicKeys = fetchApplePublicKeys();

            // idToken을 decode하여 header에서 kid 가져오기
            DecodedJWT decodedJWT = JWT.decode(identityToken);
            String kid = decodedJWT.getHeaderClaim("kid").asString();
            String alg = decodedJWT.getAlgorithm();

            // kid와 일치하는 공개키 찾기
            RSAPublicKey publicKey = findPublicKey(publicKeys, kid, alg);

            if (publicKey == null) {
                throw new PublicKeyNotFoundException();
            }

            // 서명 검증
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey, null))
                    .withAudience(appleOAuth2Properties.getClientId()) // 클라이언트 ID 매칭
                    .withIssuer(appleOAuth2Properties.getIssuer())
                    .build();

            // idToken 검증
            verifier.verify(identityToken);

            DecodedJWT verifiedJWT = verifier.verify(identityToken); // 검증된 JWT

            // 5. 서버에서 nonce 해싱 후 비교
            String tokenNonce = verifiedJWT.getClaim("nonce").asString();
            String hashedNonce = sha256(clientNonce); // SHA256 해시 함수 필요

            if (!hashedNonce.equals(tokenNonce)) {
                throw new NonceInvalidException();
            }

            // 유효하다면 true 반환
            return true;
        } catch (JWTVerificationException e) {
            // 예외 발생 시 false 반환
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> fetchApplePublicKeys() {
        // Apple 공개키 API에서 키 목록 가져오기
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(appleOAuth2Properties.getPublicKeysUrl(), Map.class);  // Map.class로 수정
    }

    private RSAPublicKey findPublicKey(Map<String, Object> publicKeys, String kid, String alg) {
        try {
            List<Map<String, String>> keys = (List<Map<String, String>>) publicKeys.get("keys");

            for (Map<String, String> key : keys) {
                if (key.get("kid").equals(kid) && key.get("alg").equals(alg)) {
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

    public String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }


    // idToken에서 사용자 정보 추출
    public UserInfoDto extractUserInfo(String idToken) {
        try {
            // id_token을 디코딩하여 Payload에 접근
            DecodedJWT decodedJWT = JWT.decode(idToken);

            // 사용자 정보 추출 (예: sub, email, name, picture 등)
            String sub = decodedJWT.getSubject();  // 사용자 고유 ID
            String email = decodedJWT.getClaim("email").asString();  // 이메일
            String name = "";  // 이름
            String picture = null;  // 프로필 사진 URL
            String provider = "apple";

            // UserInfoDto 객체 생성하여 반환
            return new UserInfoDto(sub, email, name, provider, picture);
        } catch (Exception e) {
            // 예외 처리: JWT 디코딩이나 필드 추출 실패 시 예외 처리
            throw new RuntimeException("Error extracting user info from idToken", e);
        }
    }

    // 이미 존재하는 유저인지 확인용
    public boolean isUserExist(String sub) {
        return userRepository.findBySub(sub).isPresent();
    }


    public User createUser(UserInfoDto userInfo) {
        byte[] fileBytes = null;
        Path pklFilePath = Paths.get("src/main/resources/pkl/initial_model.pkl");

        try {
            // 파일을 byte[]로 읽기
            fileBytes = Files.readAllBytes(pklFilePath);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }

        // User 객체 생성
        User newUser = User.create(
                userInfo.getSub(),
                userInfo.getEmail(),
                userInfo.getName(),
                userInfo.getUserImgUrl(),
                userInfo.getProvider(),
                fileBytes
        );

        return userRepository.save(newUser);
    }

    public User updateUser(String sub, UserInfoDto userInfo) {
        User existingUser = userRepository.findBySub(sub).orElse(null);

        existingUser.setEmail(userInfo.getEmail());  // 이메일 업데이트-
        existingUser.setProvider(userInfo.getProvider());  // 인증 제공자 업데이트

        // 3. 변경된 사용자 정보 저장 (변경 사항이 있을 때만)
        if (!existingUser.getEmail().equals(userInfo.getEmail())) {
            return userRepository.save(existingUser);
        }
        else
            return existingUser; // 바뀐거 없으면 그냥 그대로 return
    }
}
