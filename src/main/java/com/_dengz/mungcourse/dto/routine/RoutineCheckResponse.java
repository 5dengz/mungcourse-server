package com._dengz.mungcourse.dto.routine;

import com._dengz.mungcourse.entity.RoutineCheck;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RoutineCheckResponse {
    @JsonProperty("routineCheckId")
    private final Long id;

    private final Boolean isCompleted;

    private RoutineCheckResponse(Long id, Boolean isCompleted) {
        this.id = id;
        this.isCompleted = isCompleted;
    }

    public static RoutineCheckResponse create(RoutineCheck routineCheck) {
        return new RoutineCheckResponse(
                routineCheck.getId(),
                routineCheck.getIsCompleted()
        );
    }
}
