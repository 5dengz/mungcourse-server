package com._dengz.mungcourse.dto.routine;

import com._dengz.mungcourse.entity.RepeatDay;
import com._dengz.mungcourse.entity.Routine;
import com._dengz.mungcourse.entity.RoutineCheck;
import com._dengz.mungcourse.entity.RoutineSchedule;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RoutineResponse {

    @JsonProperty("routineCheckId")
    private final Long id;

    @JsonProperty("routineId")
    private final Long routineId;

    private final String name;

    private final String alarmTime;

    private final Boolean isCompleted;

    private final LocalDate date;

    private RoutineResponse(Long id, Long routineId, String name, String alarmTime, Boolean isCompleted, LocalDate date) {
        this.id = id;
        this.routineId = routineId;
        this.name = name;
        this.alarmTime = alarmTime;
        this.isCompleted = isCompleted;
        this.date = date;
    }

    public static RoutineResponse create(RoutineCheck routineCheck, Routine routine) {
        return new RoutineResponse(
                routineCheck.getId(),
                routine.getId(),
                routine.getName(),
                routine.getAlarmTime(),
                routineCheck.getIsCompleted(),
                routineCheck.getDate()
        );
    }
}
