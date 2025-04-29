package com._dengz.mungcourse.dto.walk;


import com._dengz.mungcourse.entity.DogPlace;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class WalkRecommendRequest {
    private List<DogPlace> dogPlaces;
}