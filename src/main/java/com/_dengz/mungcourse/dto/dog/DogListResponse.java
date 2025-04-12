package com._dengz.mungcourse.dto.dog;

import com._dengz.mungcourse.entity.Dog;
import lombok.Getter;

@Getter
public class DogListResponse {
    private final Long id;
    private final String name;
    private final String dogImgUrl;
    private final boolean isMain;

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