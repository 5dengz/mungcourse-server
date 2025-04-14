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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final DogService dogService;

    @Transactional
    public WalkResponse saveWalk(WalkRequest walkRequest, User user) {
        // 강아지 조회 및 유효성 검사
        List<Dog> dogs = new ArrayList<>();

        for (Long id : walkRequest.getDogIds()) {
            Dog dog = dogService.findAndCheckDogById(id, user);

            dogs.add(dog); // 검증 통과한 강아지만 리스트에 추가
        }

        // gpsData를 json 형식 문자열로 파싱
        String gpsJson = gpsSerializate(walkRequest);

        Walk walk = Walk.create(walkRequest.getDistanceKm(), walkRequest.getDurationSec(),
                walkRequest.getCalories(), gpsJson, walkRequest.getStartedAt(), walkRequest.getEndedAt(), user);

        walkRepository.save(walk);

        // WalkDog 중간 엔티티 저장
        for (Dog dog : dogs) {
            walkDogRepository.save(WalkDog.create(walk, dog));
        }

        return WalkResponse.create(walk, dogs, walkRequest.getGpsData());
    }


    @Transactional(readOnly = true)
    public List<WalkResponse> findWalksByDate(LocalDate date, User user) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Walk> walks = walkRepository.findAllByUserAndStartedAtBetween(user, startOfDay, endOfDay);

        if (walks.isEmpty()) {
            throw new WalkNotFoundException();
        }

        return walks.stream().map(walk -> { // walk는 walks 리스트에 있는 데이터 하나이고 해당 리스트 반복해서 walkResponse의 List를 만듦
            List<Dog> dogs = walkDogRepository.findAllByWalk(walk)
                    .stream()
                    .map(WalkDog::getDog)
                    .toList();

            List<WalkRequest.GpsPoint> gpsPoints = gpsDeserializate(walk);

            return WalkResponse.create(walk, dogs, gpsPoints);
        }).collect(Collectors.toList()); // 이거로 리스트화
    }


    @Transactional(readOnly = true)
    public WalkResponse searchWalkDetail(Long id, User user) {
        Walk walk = findWalkAndCheckById(id, user);

        // 중간 테이블을 통해 강아지 ID 리스트 조회
        List<Dog> dogs = walkDogRepository.findAllByWalk(walk)
                .stream()
                .map(WalkDog::getDog)
                .toList();

        // GPS 데이터 역직렬화. 즉 json 리스트 형식으로 변환
        List<WalkRequest.GpsPoint> gpsPoints = gpsDeserializate(walk);

        return WalkResponse.create(walk, dogs, gpsPoints);
    }

    @Transactional(readOnly = true)
    public List<WalkSimpleResponse> getWalksByDogId(Long id, User user) {
        Dog dog = dogService.findAndCheckDogById(id, user);

        List<WalkDog> walkDogs = walkDogRepository.findAllByDog(dog);

        return walkDogs.stream()
                .map(WalkDog::getWalk)
                .map(WalkSimpleResponse::create)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteWalk(Long id, User user) {
        Walk walk = findWalkAndCheckById(id, user);

        walkRepository.delete(walk); // cascade에 의해 WalkDog도 함께 삭제됨
    }

    public Walk findWalkAndCheckById(Long id, User user) {
        Walk walk = walkRepository.findById(id)
                .orElseThrow(WalkNotFoundException::new);

        if (!walk.getUser().getId().equals(user.getId())) {
            throw new WalkAccessForbiddenException();
        }

        return walk;
    }

    public String gpsSerializate (WalkRequest walkRequest) {
        try {
            String gpsJson = objectMapper.writeValueAsString(walkRequest.getGpsData());
            return gpsJson;
        } catch (JsonProcessingException e) {
            throw new GpsSerializationFailedException();
        }
    }

    public List<WalkRequest.GpsPoint> gpsDeserializate (Walk walk) {
        try {
            List<WalkRequest.GpsPoint> gpsPoints = objectMapper.readValue(walk.getGpsData(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, WalkRequest.GpsPoint.class));
            return gpsPoints;
        } catch (JsonProcessingException e) {
            throw new GpsDeserializationFailedException();
        }
    }
}
