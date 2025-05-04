package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.BaseResponse;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.walk.*;
import com._dengz.mungcourse.jwt.UserPrincipal;
import com._dengz.mungcourse.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Walk", description = "산책 관련 API")
@RestController
@RequestMapping("v1/walks")
@RequiredArgsConstructor
public class WalkController {

    private final WalkService walkService;

    @PostMapping()
    @Operation(summary = "산책 기록 저장", description = "프론트에서 넘겨주는 산책의 정보를 저장합니다.")
    public DataResponse<WalkResponse> saveWalk(@RequestBody WalkRequest walkRequest, @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(walkService.saveWalk(walkRequest, principal.getUser()));
    }

    @GetMapping("/calender")
    @Operation(summary = "특정 연도의 월별 산책 날짜 조회", description = "YYYY-MM 형식의 date 파라미터로 해당 기간의 산책 날짜들을 조회합니다.")
    public DataResponse<List<WalkDateResponse>> getWalksByYearAndMonth(@RequestParam("yearAndMonth") String yearAndMonth,
                                                                       @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(walkService.findWalksByYearAndMonth(yearAndMonth, principal.getUser()));
    }

    @GetMapping("")
    @Operation(summary = "특정 날짜의 산책 기록 조회", description = "YYYY-MM-DD 형식의 date 파라미터로 해당 날짜의 산책 기록을 조회합니다.")
    public DataResponse<List<WalkResponse>> getWalksByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                           @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(walkService.findWalksByDate(date, principal.getUser()));
    }

    @GetMapping("/{walkId}")
    @Operation(summary = "산책 기록 상세 조회", description = "산책 ID를 통해 산책의 상세 정보를 조회합니다.")
    public DataResponse<WalkResponse> getWalkDetail(@PathVariable("walkId") Long id, @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(walkService.searchWalkDetail(id, principal.getUser()));
    }

    @DeleteMapping("/{walkId}")
    @Operation(summary = "산책 기록 삭제", description = "특정 산책 기록을 삭제합니다.")
    public BaseResponse deleteWalk(@PathVariable("walkId") Long id, @AuthenticationPrincipal UserPrincipal principal) {
        walkService.deleteWalk(id, principal.getUser());
        return DataResponse.ok("산책 기록이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/recommend")
    @Operation(summary = "AI 기반 산책로 추천", description = "사용자의 산책 기록에 기반하여 특정 산책로를 추천합니다.")
    public DataResponse<List<WalkRecommendResponse>> findWalksRecommend(@RequestBody WalkRecommendRequest walkRecommendRequest,
                                                                  @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(walkService.searchRecommendWalks(walkRecommendRequest, principal.getUser().getPklFile()));
    }

    @GetMapping("/smokingzone")
    @Operation(summary = "산책 시작 위치 기준 2km 반경 흡연 구역 위치 전송", description = "클라이언트의 산책 시작 위치에서 근처 흡연 구역의 위치를 전달합니다.")
    public DataResponse<List<WalkSmokingZoneResponse>> findSmokingZones(@RequestParam("currentLat") Double currentLat,
                                                                      @RequestParam("currentLng") Double currentLng) {
        return DataResponse.ok(walkService.searchSmokingZones(currentLat, currentLng));
    }
}
