package com._dengz.mungcourse.dto.image;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ImageRequest {

    @NotBlank
    private String fileName;
    @NotBlank
    private String fileNameExtension;
}