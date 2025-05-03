package com._dengz.mungcourse.dto.walk;

import lombok.Getter;

@Getter
public class WalkSmokingZoneResponse {
    private final Double lat;
    private final Double lng;

    private WalkSmokingZoneResponse(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public static WalkSmokingZoneResponse create(Double lat, Double lng) {
        return new WalkSmokingZoneResponse(lat, lng);
    }
}
