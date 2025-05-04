package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.BaseResponse;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.dog.request.DogRequest;
import com._dengz.mungcourse.dto.dog.request.DogUpdateRequest;
import com._dengz.mungcourse.dto.dog.response.DogListResponse;
import com._dengz.mungcourse.dto.dog.response.DogResponse;
import com._dengz.mungcourse.dto.dog.response.MainDogResponse;
import com._dengz.mungcourse.dto.walk.WalkSimpleResponse;
import com._dengz.mungcourse.jwt.UserPrincipal;
import com._dengz.mungcourse.service.DogService;
import com._dengz.mungcourse.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final WalkService walkService;

    @GetMapping()
    @Operation(summary = "등록한 강아지 전부 가져오기", description = "유저의 강아지를 모두 가져옵니다.")
    public DataResponse<List<DogListResponse>> findDogList(@AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(dogService.searchAllDogs(principal.getUser()));
    }

    @PostMapping()
    @Operation(summary = "강아지 정보 등록하기", description = "유저의 강아지를 등록합니다, 첫 등록이면 메인강아지로 등록합니다.")
    public DataResponse<DogResponse> saveDog(@RequestBody @Valid DogRequest dogRequest,
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

    @PatchMapping("/{dogId}")
    @Operation(summary = "강아지 정보 수정", description = "강아지의 프로필을 수정합니다.")
    public DataResponse<DogResponse> updateDogDetail(@PathVariable("dogId") Long id,
                                                     @RequestBody @Valid DogUpdateRequest dogUpdateRequest,
                                                     @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(dogService.updateDog(id, dogUpdateRequest, principal.getUser()));
    }

    @DeleteMapping("/{dogId}")
    @Operation(summary = "강아지 정보 삭제", description = "강아지를 삭제하고, 메인 강아지를 삭제한 경우 가장 오래된 강아지를 메인으로 설정합니다.")
    public BaseResponse deleteDog(@PathVariable("dogId") Long id, @AuthenticationPrincipal UserPrincipal principal) {
        dogService.deleteDog(id, principal.getUser());
        return DataResponse.ok("강아지 정보가 성공적으로 삭제되었습니다.");
    }

    @PatchMapping("/{dogId}/main")
    @Operation(summary = "대표 강아지 설정", description = "dogId에 해당하는 강아지를 대표 강아지로 설정하고, 기존 강아지는 취소합니다.")
    public DataResponse<DogResponse> changeMainDog(@PathVariable("dogId") Long id, @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(dogService.setMainDog(id, principal.getUser()));
    }

    @GetMapping("/{dogId}/walks")
    @Operation(summary = "특정 강아지의 산책 기록 조회", description = "해당 dogId의 강아지 산책 기록을 가져옵니다.")
    public DataResponse<List<WalkSimpleResponse>> searchSimpleWalksByDogId(@PathVariable("dogId") Long id,
                                                                           @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(walkService.getWalksByDogId(id, principal.getUser()));
    }
}
