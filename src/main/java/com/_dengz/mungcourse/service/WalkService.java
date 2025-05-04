package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.ai.LatAndLng;
import com._dengz.mungcourse.dto.ai.WalkRecommendAiRequest;
import com._dengz.mungcourse.dto.walk.*;
import com._dengz.mungcourse.entity.*;
import com._dengz.mungcourse.exception.*;
import com._dengz.mungcourse.properties.AiServerProperties;
import com._dengz.mungcourse.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalkService {

    private final WalkRepository walkRepository;
    private final WalkDogRepository walkDogRepository;
    private final ObjectMapper objectMapper;
    private final DogService dogService;
    private final AiServerProperties aiServerProperties;
    private final DogPlaceRepository dogPlaceRepository;
    private final SmokingZoneRepository smokingZoneRepository;

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
                walkRequest.getCalories(), gpsJson, walkRequest.getStartedAt(), walkRequest.getEndedAt(), walkRequest.getRouteRating(), user);

        walkRepository.save(walk);

        // WalkDog 중간 엔티티 저장
        for (Dog dog : dogs) {
            walkDogRepository.save(WalkDog.create(walk, dog));
        }

        return WalkResponse.create(walk, dogs, walkRequest.getGpsData());
    }


    @Transactional(readOnly = true)
    public List<WalkDateResponse> findWalksByYearAndMonth(String yearAndMonth, User user) {
        YearMonth ym = YearMonth.parse(yearAndMonth);
        LocalDateTime startDayOfMonth = ym.atDay(1).atStartOfDay();
        LocalDateTime endDayOfMonth = ym.atEndOfMonth().atTime(23, 59, 59);

        List<Walk> walks = walkRepository.findAllByUserAndStartedAtBetween(user, startDayOfMonth, endDayOfMonth);

        if (walks.isEmpty()) {
            throw new WalkNotFoundException();
        }

        return walks.stream()
                .map(WalkDateResponse::create)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WalkResponse findWalkRecent(User user) {
        return walkRepository.findTopByUserOrderByStartedAtDesc(user)
                .map(walk -> {
                    List<Dog> dogs = walkDogRepository.findAllByWalk(walk)
                            .stream()
                            .map(WalkDog::getDog)
                            .toList();

                    List<WalkRequest.GpsPoint> gpsPoints = gpsDeserializate(walk);

                    return WalkResponse.create(walk, dogs, gpsPoints);
                })
                .orElse(null);
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

    public List<WalkRecommendResponse> searchRecommendWalks(WalkRecommendRequest walkRecommendRequest, byte[] pklFile) {

        LatAndLng startLocation = LatAndLng.create(walkRecommendRequest.getCurrentLat(), walkRecommendRequest.getCurrentLng());

        List<LatAndLng> waypoints;

        if (walkRecommendRequest.getDogPlaceIds() == null) {
            waypoints = Collections.emptyList();
        }

        else {
            List<DogPlace> dogPlaces = dogPlaceRepository.findAllById(walkRecommendRequest.getDogPlaceIds());

            waypoints = dogPlaces.stream()
                    .map(dogPlace -> LatAndLng.create(dogPlace.getLat(), dogPlace.getLng()))
                    .collect(Collectors.toList()); // 경유지가 있으면 해당 장소들 list로 변환해서 wayPoints에 저장
        }


        WalkRecommendAiRequest walkRecommendAiRequest = WalkRecommendAiRequest.create(startLocation, waypoints);

        // JSON 문자열 생성 (내용은 JSON 포맷이지만 multipart의 텍스트 파트로 넣음)
        String jsonString;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(walkRecommendAiRequest);
        } catch (JsonProcessingException e) {
            throw new AiRequestSerializationFailedException();
        }

        // pkl 바이너리 값을 pkl 파일로 변환 (이름은 model.pkl)
        ByteArrayResource pklResource = new ByteArrayResource(pklFile) {
            @Override
            public String getFilename() {
                return "model.pkl";
            }
        };


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("model_file", pklResource);
        body.add("json_str", jsonString); // 그냥 문자열로 추가하면 OK

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(
                aiServerProperties.getServer().getRequestUrl().getRecommend(),
                requestEntity,
                String.class
        );

        String escapedMessage = response.getBody(); // 혹은 response.getMessage()

// Jackson 또는 String replace를 사용하여 이스케이프 제거
        ObjectMapper objectMapper = new ObjectMapper();
        String unescapedJson;
        try {
            unescapedJson = objectMapper.readValue(escapedMessage, String.class);
        }
        catch (JsonProcessingException e) {
            throw new GpsDeserializationFailedException();
        }

        return gpsDeserializate(unescapedJson); // 전체 JSON 응답 문자열
    }

    public List<WalkSmokingZoneResponse> searchSmokingZones(Double currentLat, Double currentLng) {
        double radiusMeters = 2000.0; // 현재 위치에서 5km를 기준으로 검색함

        double latRange = radiusMeters / 111000.0; // 위도 1도 ≈ 111km = 111000m
        double lngRange = radiusMeters / (111000.0 * Math.cos(Math.toRadians(currentLat)));

        double minLat = currentLat - latRange;
        double maxLat = currentLat + latRange;
        double minLng = currentLng - lngRange;
        double maxLng = currentLng + lngRange;

        List<SmokingZone> smokingZones = smokingZoneRepository.findAllByLatBetweenAndLngBetween(minLat, maxLat, minLng, maxLng);

        return smokingZones.stream()
                .map(smokingZone -> {
                    return WalkSmokingZoneResponse.create(smokingZone.getLat(), smokingZone.getLng());
                })
                .collect(Collectors.toList());
    }


    public Walk findWalkAndCheckById(Long id, User user) {
        Walk walk = walkRepository.findById(id)
                .orElseThrow(WalkNotFoundException::new);

        if (!walk.getUser().getId().equals(user.getId())) {
            throw new WalkAccessForbiddenException();
        }

        return walk;
    }

    public String gpsSerializate(WalkRequest walkRequest) {
        try {
            String gpsJson = objectMapper.writeValueAsString(walkRequest.getGpsData());
            return gpsJson;
        } catch (JsonProcessingException e) {
            throw new GpsSerializationFailedException();
        }
    }

    public List<WalkRequest.GpsPoint> gpsDeserializate(Walk walk) {
        try {
            List<WalkRequest.GpsPoint> gpsPoints = objectMapper.readValue(walk.getGpsData(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, WalkRequest.GpsPoint.class));
            return gpsPoints;
        } catch (JsonProcessingException e) {
            throw new GpsDeserializationFailedException();
        }
    }

    public List<WalkRecommendResponse> gpsDeserializate(String walkRecommendResponseListString) {
        try {
            return objectMapper.readValue(
                    walkRecommendResponseListString,
                    new TypeReference<List<WalkRecommendResponse>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new GpsDeserializationFailedException();
        }
    }


}
