package com._dengz.mungcourse.dto.dog;

import com._dengz.mungcourse.entity.Dog;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MainDogResponse {

    private final String name;
    private final String dogImgUrl;

    @JsonProperty("isMain")
    private final Boolean isMain;

    private MainDogResponse(String name, String dogImgUrl, boolean isMain) {
        this.name = name;
        this.dogImgUrl = dogImgUrl;
        this.isMain = isMain;
    }

    public static MainDogResponse create(Dog dog) {
        return new MainDogResponse(
                dog.getName(),
                dog.getDogImgUrl(),
                dog.getIsMain()
        );
    }
}