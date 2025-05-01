package com._dengz.mungcourse.dto.routine;

import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.RepeatDay;
import com._dengz.mungcourse.entity.Routine;
import com._dengz.mungcourse.entity.RoutineSchedule;
import lombok.Getter;

import java.util.List;

@Getter
public class RoutinePostResponse {

    private final Long id;

    private final String name;

    private final String alarmTime;

    private final List<RepeatDay> repeatDays;

    private RoutinePostResponse(Long id, String name, String alarmTime, List<RepeatDay> repeatDays) {
        this.id = id;
        this.name = name;
        this.alarmTime = alarmTime;
        this.repeatDays = repeatDays;
    }

    public static RoutinePostResponse create(Routine routine, List<RoutineSchedule> routineSchedules) {
        return new RoutinePostResponse(
                routine.getId(),
                routine.getName(),
                routine.getAlarmTime(),
                routineSchedules.stream().map(RoutineSchedule::getRepeatDay).toList());
    }
}
