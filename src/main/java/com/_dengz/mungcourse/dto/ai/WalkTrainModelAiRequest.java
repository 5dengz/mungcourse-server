package com._dengz.mungcourse.dto.ai;

import com._dengz.mungcourse.dto.walk.WalkRequest;
import com._dengz.mungcourse.entity.Walk;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.List;

@Getter
public class WalkTrainModelAiRequest {

    @JsonProperty("gps_list")
    private List<WalkRequest.GpsPoint> gpsList;

    private Float label;

    private WalkTrainModelAiRequest(List<WalkRequest.GpsPoint> gpsList, Float label) {
        this.gpsList = gpsList;
        this.label = label;
    }

    public static WalkTrainModelAiRequest create(Walk walk) {
        // walk.getGpsData() → JSON 문자열이라면 → 여기서 List<GpsPoint>로 파싱
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<WalkRequest.GpsPoint> parsed = mapper.readValue(
                    walk.getGpsData(),
                    new TypeReference<List<WalkRequest.GpsPoint>>() {
                    }
            );
            return new WalkTrainModelAiRequest(parsed, walk.getRouteRating());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GPS JSON 파싱 실패", e);
        }
    }
}
