package com._dengz.mungcourse.dto.walk;


import com._dengz.mungcourse.entity.DogPlace;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class WalkRecommendRequest {

    @NotNull
    private Double currentLat;

    @NotNull
    private Double currentLng;

    private List<Long> dogPlaceIds;
}