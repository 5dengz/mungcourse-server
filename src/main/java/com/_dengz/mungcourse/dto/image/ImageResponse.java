package com._dengz.mungcourse.dto.image;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageResponse {

    private String key;
    private String preSignedUrl;
    private String url;

    private ImageResponse(String key, String preSignedUrl, String url) {
        this.key = key;
        this.preSignedUrl = preSignedUrl;
        this.url = url;
    }

    public static ImageResponse create(String key, String preSignedUrl, String url) {
        return new ImageResponse(key, preSignedUrl, url);
    }
}