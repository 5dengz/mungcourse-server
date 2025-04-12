package com._dengz.mungcourse.dto.dog;

import com._dengz.mungcourse.entity.Dog;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DogResponse {
    private final String name;
    @NotBlank
    private final String gender;
    private final String breed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(example = "2024-01-01", type = "string")
    private final LocalDate birthDate;

    private final Float weight;

    private final Boolean hasArthritis;
    private final Boolean neutered ;
    private final String dogImgUrl;
    private final Boolean isMain;

    private DogResponse(String name, String gender, String breed, LocalDate birthDate,
                        Float weight, Boolean hasArthritis, Boolean neutered, String dogImgUrl, Boolean isMain)
    {
        this.name = name;
        this.gender = gender;
        this.breed = breed;
        this.birthDate = birthDate;
        this.weight = weight;
        this.hasArthritis = hasArthritis;
        this.neutered = neutered;
        this.dogImgUrl = dogImgUrl;
        this.isMain = isMain;
    }

    public static DogResponse create(Dog dog) {
        return new DogResponse(
                dog.getName(),
                dog.getGender(),
                dog.getBreed(),
                dog.getBirthDate(),
                dog.getWeight(),
                dog.getHasArthritis(),
                dog.getNeutered(),
                dog.getDogImgUrl(),
                dog.getIsMain()
        );
    }
}
