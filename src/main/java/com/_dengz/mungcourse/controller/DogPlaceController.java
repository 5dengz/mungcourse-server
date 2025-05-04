package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.dogPlace.DogPlaceListResponse;
import com._dengz.mungcourse.service.DogPlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "DogPlace", description = "강아지 동반 가능 시설 API")
@RestController
@RequestMapping("v1/dogPlaces")
@RequiredArgsConstructor
public class DogPlaceController {
    private final DogPlaceService dogPlaceService;

    @GetMapping()
    @Operation(summary = "현재 위치 기준 2km 반경 장소 검색", description = "클라이언트의 현재 위치에서 가까운 애견 동반 시설들을 검색합니다, 카테고리를 기준으로 필터링 할 수도 있습니다.")
    public DataResponse<List<DogPlaceListResponse>> getDogPlacessByCurrentLatAndLng(@RequestParam("currentLat") Double currentLat,
                                                                                    @RequestParam("currentLng") Double currentLng,
                                                                                    @RequestParam(value = "category", required = false)
                                                                                        String category) {
        return DataResponse.ok(dogPlaceService.searchNearDogPlaceList(currentLat, currentLng, category));
    }

    @GetMapping("/search")
    @Operation(summary = "이름으로 장소 검색", description = "장소 이름으로 특정 애견 동반 시설을 검색합니다.")
    public DataResponse<List<DogPlaceListResponse>> getDogPlacessByName(@RequestParam("currentLat") Double currentLat,
                                                                        @RequestParam("currentLng") Double currentLng,
                                                                        @RequestParam("placeName") String name) {
        return DataResponse.ok(dogPlaceService.searchNearDogPlaceListByName(currentLat, currentLng, name));
    }


}
