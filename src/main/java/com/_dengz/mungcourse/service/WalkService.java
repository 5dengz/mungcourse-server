package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.walk.WalkRequest;
import com._dengz.mungcourse.dto.walk.WalkResponse;
import com._dengz.mungcourse.dto.walk.WalkSimpleResponse;
import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.entity.Walk;
import com._dengz.mungcourse.entity.WalkDog;
import com._dengz.mungcourse.exception.GpsSerializationFailedException;
import com._dengz.mungcourse.exception.GpsDeserializationFailedException;
import com._dengz.mungcourse.repository.DogRepository;
import com._dengz.mungcourse.exception.DogNotFoundException;
import com._dengz.mungcourse.exception.DogAccessForbiddenException;
import com._dengz.mungcourse.exception.WalkNotFoundException;
import com._dengz.mungcourse.exception.WalkAccessForbiddenException;
import com._dengz.mungcourse.repository.WalkDogRepository;
import com._dengz.mungcourse.repository.WalkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public WalkResponse searchWalkDetail(Long id, User user) {
        Walk walk = walkRepository.findById(id)
                .orElseThrow(WalkNotFoundException::new);

        if (!walk.getUser().getId().equals(user.getId())) {
            throw new WalkAccessForbiddenException(); // 자신 소유가 아닌 walk 조회 방지
        }

        // 중간 테이블을 통해 강아지 ID 리스트 조회
        List<Dog> dogs = walkDogRepository.findAllByWalk(walk)
                .stream()
                .map(WalkDog::getDog)
                .toList();

        // GPS 데이터 역직렬화
        List<WalkRequest.GpsPoint> gpsPoints;
        try {
            gpsPoints = objectMapper.readValue(walk.getGpsData(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, WalkRequest.GpsPoint.class));
        } catch (JsonProcessingException e) {
            throw new GpsDeserializationFailedException(); // 따로 정의 필요
        }

        return WalkResponse.create(walk, dogs, gpsPoints);
    }

    @Transactional(readOnly = true)
    public List<WalkSimpleResponse> getWalksByDogId(Long id, User user) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new);

        if (!dog.getUser().getId().equals(user.getId())) {
            throw new DogAccessForbiddenException();
        }

        List<WalkDog> walkDogs = walkDogRepository.findAllByDog(dog);

        return walkDogs.stream()
                .map(WalkDog::getWalk)
                .map(WalkSimpleResponse::create)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteWalk(Long id, User user) {
        Walk walk = walkRepository.findById(id)
                .orElseThrow(WalkNotFoundException::new);

        if (!walk.getUser().getId().equals(user.getId())) {
            throw new WalkAccessForbiddenException();
        }

        walkRepository.delete(walk); // cascade에 의해 WalkDog도 함께 삭제됨
    }
}
