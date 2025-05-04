package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.BaseResponse;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.routine.*;
import com._dengz.mungcourse.jwt.UserPrincipal;
import com._dengz.mungcourse.service.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Routine", description = "루틴 관련 API")
@RestController
@RequestMapping("v1/routines")
@RequiredArgsConstructor
public class RoutineController {
    private final RoutineService routineService;

    @GetMapping()
    @Operation(summary = "날짜를 기준으로 루틴들 검색", description = "YYYY-MM-DD 형식의 date 파라미터로 특정 날짜의 루틴들을 검색합니다.")
    public DataResponse<List<RoutineResponse>> searchRoutinesByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                        LocalDate date,
                                                                    @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(routineService.findRoutineByDate(date, principal.getUser()));
    }

    @PostMapping()
    @Operation(summary = "특정 루틴 등록", description = "사용자가 루틴을 등록합니다.")
    public DataResponse<RoutinePostResponse> saveRoutine(@RequestBody RoutineRequest routineRequest,
                                                         @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(routineService.makeRoutine(routineRequest, principal.getUser()));
    }

    @GetMapping("/{routineId}")
    @Operation(summary = "루틴의 등록된 정보 검색", description = "등록된 루틴의 정보를 검색합니다. 루틴 변경 페이지에 사용됩니다.")
    public DataResponse<RoutinePostResponse> searchRoutinesDetail(@PathVariable("routineId") Long id,
                                                                  @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(routineService.findRoutineDetail(id, principal.getUser()));
    }

    @PatchMapping("/{routineId}")
    @Operation(summary = "등록된 루틴을 변경", description = "등록된 루틴의 정보를 변경합니다, 해당 루틴의 날짜 기준으로 이후 날짜만 변경합니다.")
    public DataResponse<RoutinePostResponse> updateRoutineDetail(@PathVariable("routineId") Long id,
                                                                 @RequestBody RoutineUpdateRequest routineUpdateRequest,
                                                           @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(routineService.updateRoutine(id, routineUpdateRequest, principal.getUser()));
    }

    @DeleteMapping("/{routineId}")
    @Operation(summary = "등록된 루틴을 삭제", description = "등록된 루틴을 삭제합니다. 해당되는 모든 루틴 기록을 삭제합니다.")
    public BaseResponse deleteRoutine(@PathVariable("routineId") Long id,
                                      @AuthenticationPrincipal UserPrincipal principal) {
        routineService.deleteRoutine(id, principal.getUser());
        return DataResponse.ok("루틴을 성공적으로 삭제했습니다.");
    }

    @PatchMapping("/{routineCheckId}/toggle")
    @Operation(summary = "루틴 체크 OR 체크 해제", description = "사용자가 완료한 루틴을 체크하거나 체크 해제합니다.")
    public DataResponse<RoutineCheckResponse> toggleRoutine(@PathVariable("routineCheckId") Long id,
                                                            @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(routineService.toggleRoutine(id, principal.getUser()));
    }

}
