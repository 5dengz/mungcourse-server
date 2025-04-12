package com._dengz.mungcourse.dto.dog;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class DogRequest {


    private String name;
    @NotBlank
    private String gender;
    private String breed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(example = "2024-01-01", type = "string")
    private LocalDate birthDate;

    private Float weight;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postedAt = LocalDateTime.now();

    private Boolean hasArthritis;
    private Boolean neutered ;
    private String dogImgUrl;
}
