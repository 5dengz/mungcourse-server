package com._dengz.mungcourse.dto.dogPlace;


import com._dengz.mungcourse.entity.DogPlace;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DogPlaceListResponse {
    @NotBlank
    private final String name;

    private final String dogPlaceImgUrl;

    private final Double distance;

    private DogPlaceListResponse(String name, String dogPlaceImgUrl, Double distance) {
        this.name = name;
        this.dogPlaceImgUrl = dogPlaceImgUrl;
        this.distance = distance;
    }

    public static DogPlaceListResponse create(DogPlace dogPlace, Double distance) {
        return new DogPlaceListResponse(
                dogPlace.getName(),
                dogPlace.getPlaceImgUrl(),
                distance
        );
    }
}
