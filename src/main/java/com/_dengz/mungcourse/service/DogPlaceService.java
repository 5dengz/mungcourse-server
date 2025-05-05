package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.dogPlace.DogPlaceListResponse;
import com._dengz.mungcourse.entity.DogPlace;
import com._dengz.mungcourse.repository.DogPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogPlaceService {

    private final DogPlaceRepository dogPlaceRepository;

    public List<DogPlaceListResponse> searchNearDogPlaceList(double currentLat, double currentLng, String category) {
        double radiusMeters = 2000.0; // 현재 위치에서 5km를 기준으로 검색함

        double latRange = radiusMeters / 111000.0; // 위도 1도 ≈ 111km = 111000m
        double lngRange = radiusMeters / (111000.0 * Math.cos(Math.toRadians(currentLat)));

        double minLat = currentLat - latRange;
        double maxLat = currentLat + latRange;
        double minLng = currentLng - lngRange;
        double maxLng = currentLng + lngRange;

        List<DogPlace> dogPlaces = dogPlaceRepository.findAllByLatBetweenAndLngBetween(minLat, maxLat, minLng, maxLng);

        return dogPlaces.stream()// dogPlaces로부터 dogPlaceListResponse 만들어줌
                .map(dogplace -> {
                    double distance = haversine(currentLat, currentLng, dogplace.getLat(), dogplace.getLng()); // 거리 계산 메서드
                    return DogPlaceListResponse.create(dogplace, distance);
                })
                .filter(response -> response.getDistance() <= radiusMeters) // 2000m 이하만 한번 더 정렬
                .filter(place -> category == null || category.isBlank() || place.getCategory().equals(category))
                .sorted(Comparator.comparingDouble(DogPlaceListResponse::getDistance)) // 가까운 순서로 정렬
                .collect(Collectors.toList());
    }

    public List<DogPlaceListResponse> searchNearDogPlaceListByName(double currentLat, double currentLng, String name) {
        double radiusMeters = 2000.0; // 현재 위치에서 5km를 기준으로 검색함

        double latRange = radiusMeters / 111000.0; // 위도 1도 ≈ 111km = 111000m
        double lngRange = radiusMeters / (111000.0 * Math.cos(Math.toRadians(currentLat)));

        double minLat = currentLat - latRange;
        double maxLat = currentLat + latRange;
        double minLng = currentLng - lngRange;
        double maxLng = currentLng + lngRange;

        List<DogPlace> dogPlaces = dogPlaceRepository.findAllByLatBetweenAndLngBetween(minLat, maxLat, minLng, maxLng);

        return dogPlaces.stream()// dogPlaces로부터 dogPlaceListResponse 만들어줌
                .filter(place -> place.getName().equals(name))
                .map(dogplace -> {
                    double distance = haversine(currentLat, currentLng, dogplace.getLat(), dogplace.getLng()); // 거리 계산 메서드
                    return DogPlaceListResponse.create(dogplace, distance);
                })
                .filter(response -> response.getDistance() <= radiusMeters) // 2000m 이하만 한번 더 정렬
                .sorted(Comparator.comparingDouble(DogPlaceListResponse::getDistance)) // 가까운 순서로 정렬
                .collect(Collectors.toList());
    }


    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371000; // 지구 반지름 (단위: meter)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 단위: meter
    }
}
