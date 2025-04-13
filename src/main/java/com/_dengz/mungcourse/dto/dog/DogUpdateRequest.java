package com._dengz.mungcourse.dto.dog;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class DogUpdateRequest {

    private String name;
    private String gender;
    private String breed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(example = "2024-01-01", type = "string")
    private LocalDate birthDate;

    private Float weight;

    private Boolean hasArthritis;
    private Boolean neutered;
    private String dogImgUrl;
}