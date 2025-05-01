package com._dengz.mungcourse.dto.walk;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class WalkRecommendResponse {
    private final List<List<WalkRequest.GpsPoint>> gpsDataList;

    private WalkRecommendResponse(List<List<WalkRequest.GpsPoint>> gpsDataList) {
        this.gpsDataList = gpsDataList;
    }

    public static WalkRecommendResponse create(List<List<WalkRequest.GpsPoint>> gpsDataList) {
        return new WalkRecommendResponse(gpsDataList);
    }
}
