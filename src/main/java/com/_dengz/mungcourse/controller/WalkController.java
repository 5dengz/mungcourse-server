package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.BaseResponse;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.walk.WalkRequest;
import com._dengz.mungcourse.dto.walk.WalkResponse;
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

    @GetMapping()
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
        return DataResponse.ok("성공적으로 삭제되었습니다.");
    }
}
