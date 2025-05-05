package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.BaseResponse;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.dogPlace.DogPlaceListResponse;
import com._dengz.mungcourse.service.DogPlaceDevService;
import com._dengz.mungcourse.service.DogPlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "DogPlaceDev", description = "강아지 동반 가능 시설 개발 전용 API")
@RestController
@RequestMapping("v1/dogPlaces-dev")
@RequiredArgsConstructor
public class DogPlaceDevController {
    private final DogPlaceDevService dogPlaceDevService;

    @GetMapping("/images")
    @Operation(summary = "애견 동반 시설 이미지 url 저장", description = "애견 동반 시설의 이미지 url을 저장합니다.")
    public BaseResponse saveDogPlaceImages(HttpServletRequest request) {
        dogPlaceDevService.saveDogImageUrl();
        return DataResponse.ok("이미지 저장 완료");
    }
}

