package com._dengz.mungcourse.dto.walk;

import lombok.Getter;

@Getter
public class WalkWarningZoneResponse {
    private final Double lat;
    private final Double lng;
    private final String category;

    private WalkWarningZoneResponse(Double lat, Double lng, String category) {
        this.lat = lat;
        this.lng = lng;
        this.category = category;
    }

    public static WalkWarningZoneResponse create(Double lat, Double lng, String category) {
        return new WalkWarningZoneResponse(lat, lng, category);
    }
}
