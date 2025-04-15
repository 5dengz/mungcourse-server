package com._dengz.mungcourse.dto.image;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ImageDeleteRequest {
    @NotBlank
    private String key;
}
