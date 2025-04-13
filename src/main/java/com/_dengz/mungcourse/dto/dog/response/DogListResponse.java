package com._dengz.mungcourse.dto.dog.response;

import com._dengz.mungcourse.entity.Dog;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DogListResponse {
    private final Long id;
    private final String name;
    private final String dogImgUrl;

    @JsonProperty("isMain")
    private final Boolean isMain;

    private DogListResponse(Long id, String name, String dogImgUrl, boolean isMain) {
        this.id = id;
        this.name = name;
        this.dogImgUrl = dogImgUrl;
        this.isMain = isMain;
    }

    public static DogListResponse create(Dog dog) {
        return new DogListResponse(
                dog.getId(),
                dog.getName(),
                dog.getDogImgUrl(),
                dog.getIsMain()
        );
    }
}