package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.dog.DogRequest;
import com._dengz.mungcourse.dto.dog.DogResponse;
import com._dengz.mungcourse.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/dogs")
@RequiredArgsConstructor
public class DogController {

    private final DogService dogService;

    @PostMapping()
    @Operation(summary = "강아지 정보 등록하기", description = "유저의 강아지를 등록합니다, 첫 등록이면 메인강아지로 등록합니다.")
    public DataResponse<DogResponse> addDog(HttpServletRequest request, @RequestBody @Valid DogRequest dogRequest) {
        return DataResponse.ok(dogService.makeDog(request, dogRequest));
    }
}
