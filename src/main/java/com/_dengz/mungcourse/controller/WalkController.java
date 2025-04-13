package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.walk.WalkRequest;
import com._dengz.mungcourse.dto.walk.WalkResponse;
import com._dengz.mungcourse.jwt.UserPrincipal;
import com._dengz.mungcourse.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Walk", description = "산책 관련 API")
@RestController
@RequestMapping("v1/walks")
@RequiredArgsConstructor
public class WalkController {

    private final WalkService walkService;

    @PostMapping()
    @Operation(summary = "산책 기록 저장")
    public DataResponse<WalkResponse> saveWalk(@RequestBody WalkRequest walkRequest,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        return DataResponse.ok(walkService.saveWalk(walkRequest, principal.getUser()));
    }

}
