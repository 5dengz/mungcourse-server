package com._dengz.mungcourse.client;

import com._dengz.mungcourse.dto.ai.WalkTrainModelAiRequest;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.properties.AiServerProperties;
import com._dengz.mungcourse.exception.AiModelTrainFailedException;
import com._dengz.mungcourse.exception.AiModelTrainRequestSerializationFailedExcepiton;
import com._dengz.mungcourse.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiClient {

    private final UserRepository userRepository;
    private final AiServerProperties aiServerProperties;

    public void sendTrainingData(User user, List<WalkTrainModelAiRequest> trainDataList) {
        // 1. JSON 직렬화
        String jsonStr;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonStr = objectMapper.writeValueAsString(trainDataList);
        } catch (JsonProcessingException e) {
            throw new AiModelTrainRequestSerializationFailedExcepiton();
        }


        // 2. PKL 바이너리 → 파일 형태
        ByteArrayResource pklResource = new ByteArrayResource(user.getPklFile()) {
            @Override
            public String getFilename() {
                return "model.pkl";
            }
        };

        System.out.println(pklResource);

        // 3. Multipart 전송 구성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("model", pklResource);
        body.add("json_str", jsonStr);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        System.out.println(requestEntity);

        // 4. RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // 5. 응답을 byte[]로 받기
        ResponseEntity<byte[]> response = restTemplate.exchange(
                aiServerProperties.getServer().getRequestUrl().getTrain(),
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        System.out.println(response);

        // 6. 응답 성공 시 사용자 PKL 업데이트
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            user.setPklFile(response.getBody());
            userRepository.save(user);
        } else {
            throw new AiModelTrainFailedException();
        }
    }
}