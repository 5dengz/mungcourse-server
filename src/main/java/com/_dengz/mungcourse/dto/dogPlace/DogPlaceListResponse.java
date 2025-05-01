package com._dengz.mungcourse.dto.dogPlace;


import com._dengz.mungcourse.entity.DogPlace;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DogPlaceListResponse {

    @NotBlank
    private final Long id;

    @NotBlank
    private final String name;

    private final String dogPlaceImgUrl;

    private final Double distance;

    private final String category;

    private final String openingHours;

    private final Double lat;
    private final Double lng;



    private DogPlaceListResponse(Long id, String name, String dogPlaceImgUrl, Double distance, String category, String openingHours, Double lat, Double lng) {
        this.id = id;
        this.name = name;
        this.dogPlaceImgUrl = dogPlaceImgUrl;
        this.distance = distance;
        this.category = category;
        this.openingHours = openingHours;
        this.lat = lat;
        this.lng = lng;
    }

    public static DogPlaceListResponse create(DogPlace dogPlace, Double distance) {
        return new DogPlaceListResponse(
                dogPlace.getId(),
                dogPlace.getName(),
                dogPlace.getPlaceImgUrl(),
                distance,
                dogPlace.getCategory(),
                dogPlace.getOpeningHours(),
                dogPlace.getLat(),
                dogPlace.getLng()
        );
    }
}
