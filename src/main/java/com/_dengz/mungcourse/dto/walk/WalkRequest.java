package com._dengz.mungcourse.dto.walk;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class WalkRequest {

    private Float distanceKm;
    private Integer durationSec;
    private Integer calories;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endedAt;

    private Integer routeRating;

    private List<Long> dogIds;
    private List<GpsPoint> gpsData;

    @Getter
    public static class GpsPoint {
        private Double lat;
        private Double lng;
    }
}