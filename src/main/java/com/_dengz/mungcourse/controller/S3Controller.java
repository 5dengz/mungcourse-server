package com._dengz.mungcourse.controller;

import com._dengz.mungcourse.dto.common.BaseResponse;
import com._dengz.mungcourse.dto.common.DataResponse;
import com._dengz.mungcourse.dto.image.ImageDeleteRequest;
import com._dengz.mungcourse.dto.image.ImageRequest;
import com._dengz.mungcourse.dto.image.ImageResponse;
import com._dengz.mungcourse.jwt.UserPrincipal;
import com._dengz.mungcourse.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "S3", description = "S3 관련 API")
@RestController
@RequestMapping("v1/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping
    @Operation(summary = "이미지 preSignedUrl 요청", description = "이미지 업로드를 위한 preSignedUrl을 요청합니다.")
    public DataResponse<ImageResponse> postPreSignedURLs(@RequestBody @Valid ImageRequest imageRequest) {
        return DataResponse.ok(s3Service.makePreSignedUrls(imageRequest));
    }

    @DeleteMapping
    @Operation(summary = "프로필 사진 등록 페이지에서 이미지를 삭제합니다.", description = "프론트에서 publicUrl로 부터 key 값을 추출해서 전송하면 이미지를 삭제합니다.")
    public BaseResponse deleteImage(@RequestBody @Valid ImageDeleteRequest imageDeleteRequest,
                                    @AuthenticationPrincipal UserPrincipal principal) {
        s3Service.deleteImage(imageDeleteRequest, principal.getUser());
        return DataResponse.ok("이미지가 성공적으로 삭제되었습니다.");
    }
}
