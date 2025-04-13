package com._dengz.mungcourse.dto.walk;

import com._dengz.mungcourse.entity.Walk;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class WalkSimpleResponse {
    private Long id;
    private Float distanceKm;
    private Integer durationSec;
    private Integer calories;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endedAt;

    private WalkSimpleResponse(Long id, Float distanceKm, Integer durationSec, Integer calories,
                         LocalDateTime startedAt, LocalDateTime endedAt)
    {
        this.id = id;
        this.distanceKm = distanceKm;
        this.durationSec = durationSec;
        this.calories = calories;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static WalkSimpleResponse create(Walk walk) {
        return new WalkSimpleResponse(
                walk.getId(),
                walk.getDistanceKm(),
                walk.getDurationSec(),
                walk.getCalories(),
                walk.getStartedAt(),
                walk.getEndedAt()
        );
    }
}