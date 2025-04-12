package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.dog.DogListResponse;
import com._dengz.mungcourse.dto.dog.DogRequest;
import com._dengz.mungcourse.dto.dog.DogResponse;
import com._dengz.mungcourse.dto.dog.MainDogResponse;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.AccessTokenNotFoundException;
import com._dengz.mungcourse.exception.UserNotFoundException;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.service.AuthService;
import com._dengz.mungcourse.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/dogs")
@RequiredArgsConstructor
public class DogController {

    private final DogService dogService;
    private final TokenProvider tokenProvider;
    private final AuthService authService;

    @GetMapping()
    public DataResponse<List<DogListResponse>> findDogList(HttpServletRequest request) {
        String sub = tokenProvider.extractSub(request).orElseThrow(UserNotFoundException::new);
        return DataResponse.ok(dogService.searchAllDogs(authService.getCurrentUser(sub)));
    }

    @PostMapping()
    @Operation(summary = "강아지 정보 등록하기", description = "유저의 강아지를 등록합니다, 첫 등록이면 메인강아지로 등록합니다.")
    public DataResponse<DogResponse> addDog(HttpServletRequest request, @RequestBody @Valid DogRequest dogRequest) {
        String sub = tokenProvider.extractSub(request).orElseThrow(UserNotFoundException::new);
        return DataResponse.ok(dogService.makeDog(authService.getCurrentUser(sub), dogRequest));
    }

    @GetMapping("/main")
    public DataResponse<MainDogResponse> findDogByIsMain(HttpServletRequest request) {
        String sub = tokenProvider.extractSub(request).orElseThrow(UserNotFoundException::new);
        return DataResponse.ok(dogService.searchMainDog(authService.getCurrentUser(sub)));
    }
}
