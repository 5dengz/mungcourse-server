package com._dengz.mungcourse.dto.ai;

import com._dengz.mungcourse.dto.walk.WalkRecommendRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class WalkRecommendAiRequest {
    @JsonProperty("start_location")
    private LatAndLng startLocation;

    private List<LatAndLng> waypoints;

    private WalkRecommendAiRequest(LatAndLng startLocation, List<LatAndLng> waypoints) {
        this.startLocation = startLocation;
        this.waypoints = waypoints;
    }

    public static WalkRecommendAiRequest create(LatAndLng startLocation, List<LatAndLng> waypoints) {
        return new WalkRecommendAiRequest(startLocation, waypoints);
    }
}
