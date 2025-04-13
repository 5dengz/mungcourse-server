package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.walk.WalkRequest;
import com._dengz.mungcourse.dto.walk.WalkResponse;
import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.entity.Walk;
import com._dengz.mungcourse.entity.WalkDog;
import com._dengz.mungcourse.exception.GpsSerializationFailedException;
import com._dengz.mungcourse.repository.DogRepository;
import com._dengz.mungcourse.exception.DogNotFoundException;
import com._dengz.mungcourse.exception.DogAccessForbiddenException;
import com._dengz.mungcourse.repository.WalkDogRepository;
import com._dengz.mungcourse.repository.WalkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalkService {

    private final DogRepository dogRepository;
    private final WalkRepository walkRepository;
    private final WalkDogRepository walkDogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public WalkResponse saveWalk(WalkRequest walkRequest, User user) {
        // 1. 강아지 조회 및 유효성 검사
        List<Dog> dogs = new ArrayList<>();

        for (Long id : walkRequest.getDogIds()) {
            Dog dog = dogRepository.findById(id)
                    .orElseThrow(DogNotFoundException::new); // 없는 강아지

            if (!dog.getUser().getId().equals(user.getId())) {
                throw new DogAccessForbiddenException(); // 권한 없음
            }

            dogs.add(dog); // 검증 통과한 강아지만 리스트에 추가
        }

        // 2. gpsData를 json 형식으로 파싱
        String gpsJson;

        try {
            gpsJson = objectMapper.writeValueAsString(walkRequest.getGpsData());
        } catch (JsonProcessingException e) {
            throw new GpsSerializationFailedException();
        }

        Walk walk = Walk.create(walkRequest.getDistanceKm(), walkRequest.getDurationSec(),
                walkRequest.getCalories(), gpsJson, walkRequest.getStartedAt(), walkRequest.getEndedAt(), user);

        walkRepository.save(walk);

        // 3. WalkDog 중간 엔티티 저장
        for (Dog dog : dogs) {
            walkDogRepository.save(WalkDog.create(walk, dog));
        }

        return WalkResponse.create(walk, dogs, walkRequest.getGpsData());
    }
}
