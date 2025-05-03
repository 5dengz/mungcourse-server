package com._dengz.mungcourse.dto.ai;

import com._dengz.mungcourse.dto.walk.WalkRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WalkTrainModelAiRequest {
    @JsonProperty("gps_list")
    private List<WalkRequest.GpsPoint> gpsList;

    private Float label;

    private WalkTrainModelAiRequest(List<WalkRequest.GpsPoint> gpsList, Float label) {
        this.gpsList = gpsList;
        this.label = label;
    }

    public static WalkTrainModelAiRequest create(List<WalkRequest.GpsPoint> gpsList, Float label) {
        return new WalkTrainModelAiRequest(gpsList, label);
    }
}
