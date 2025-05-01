package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.dogPlace.DogPlaceListResponse;
import com._dengz.mungcourse.dto.routine.RoutinePostResponse;
import com._dengz.mungcourse.dto.routine.RoutineRequest;
import com._dengz.mungcourse.jwt.UserPrincipal;
import com._dengz.mungcourse.service.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Routine", description = "루틴 관련 API")
@RestController
@RequestMapping("v1/routines")
@RequiredArgsConstructor
public class RoutineController {
    private final RoutineService routineService;

    @PostMapping()
    @Operation(summary = "특정 루틴 등록", description = "사용자가 루틴을 등록합니다.")
    public DataResponse<RoutinePostResponse> saveRoutine(@RequestBody RoutineRequest routineRequest,
                                                         @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(routineService.makeRoutine(routineRequest, principal.getUser()));
    }
}
