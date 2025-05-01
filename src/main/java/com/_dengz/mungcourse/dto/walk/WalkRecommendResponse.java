package com._dengz.mungcourse.dto.walk;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class WalkRecommendResponse {

    private List<WalkRequest.GpsPoint> route;
    @JsonProperty("route_length")
    private Double routeLength;

    // 생성자
    public WalkRecommendResponse(List<WalkRequest.GpsPoint> route, Double routeLength) {
        this.route = route;
        this.routeLength = routeLength;
    }

    public static WalkRecommendResponse create(List<WalkRequest.GpsPoint> route, Double routeLength) {
        return new WalkRecommendResponse(route, routeLength);
    }
}
