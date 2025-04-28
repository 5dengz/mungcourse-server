package com._dengz.mungcourse.dto.walk;

import com._dengz.mungcourse.entity.Walk;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class WalkDateResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    private WalkDateResponse(LocalDateTime startedAt)
    {
        this.startedAt = startedAt;
    }

    public static WalkDateResponse create(Walk walk) {
        return new WalkDateResponse(
                walk.getStartedAt()
        );
    }
}
