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
import com._dengz.mungcourse.jwt.UserPrincipal;
import com._dengz.mungcourse.service.AuthService;
import com._dengz.mungcourse.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Dog", description = "강아지 관련 API")
@RestController
@RequestMapping("v1/dogs")
@RequiredArgsConstructor
public class DogController {

    private final DogService dogService;
    private final TokenProvider tokenProvider;
    private final AuthService authService;
    @GetMapping()
    @Operation(summary = "등록한 강아지 전부 가져오기", description = "유저의 강아지를 모두 가져옵니다.")
    public DataResponse<List<DogListResponse>> findDogList(@AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(dogService.searchAllDogs(principal.getUser()));
    }

    @PostMapping()
    @Operation(summary = "강아지 정보 등록하기", description = "유저의 강아지를 등록합니다, 첫 등록이면 메인강아지로 등록합니다.")
    public DataResponse<DogResponse> addDog(@RequestBody @Valid DogRequest dogRequest,
                                            @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(dogService.makeDog(dogRequest, principal.getUser()));
    }

    @GetMapping("/main")
    @Operation(summary = "메인 강아지 가져오기", description = "유저의 메인 강아지를 가져옵니다")
    public DataResponse<MainDogResponse> findDogByIsMain(@AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(dogService.searchMainDog(principal.getUser()));
    }

    @GetMapping("/{dogId}")
    @Operation(summary = "강아지 세부 정보 가져오기", description = "강아지 id값으로 강아지 세부 정보를 가져옵니다")
    public DataResponse<DogResponse> findDogDetail(@PathVariable("dogId") Long id,
                                                   @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(dogService.searchDogDetail(id, principal.getUser()));
    }
}
