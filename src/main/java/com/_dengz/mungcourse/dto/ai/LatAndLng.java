package com._dengz.mungcourse.dto.ai;

import lombok.Getter;

@Getter
public class LatAndLng {
    private Double latitude;
    private Double longitude;

    private LatAndLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LatAndLng create(Double latitude, Double longitude) {
        return new LatAndLng(latitude, longitude);
    }
}
