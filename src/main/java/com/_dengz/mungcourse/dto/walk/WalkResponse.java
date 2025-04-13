package com._dengz.mungcourse.dto.walk;

import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.Walk;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class WalkResponse {
    private Long id;
    private Float distanceKm;
    private Integer durationSec;
    private Integer calories;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private List<Long> dogIds;
    private List<WalkRequest.GpsPoint> gpsData;

    private WalkResponse(Long id, Float distanceKm, Integer durationSec, Integer calories,
                         LocalDateTime startedAt, LocalDateTime endedAt, List<Long> dogIds,
                         List<WalkRequest.GpsPoint> gpsData)
    {
        this.id = id;
        this.distanceKm = distanceKm;
        this.durationSec = durationSec;
        this.calories = calories;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.dogIds = dogIds;
        this.gpsData = gpsData;
    }

    public static WalkResponse create(Walk walk, List<Dog> dogs, List<WalkRequest.GpsPoint> gpsData) {
        return new WalkResponse(
                walk.getId(),
                walk.getDistanceKm(),
                walk.getDurationSec(),
                walk.getCalories(),
                walk.getStartedAt(),
                walk.getEndedAt(),
                dogs.stream().map(Dog::getId).toList(),
                gpsData
        );
    }
}