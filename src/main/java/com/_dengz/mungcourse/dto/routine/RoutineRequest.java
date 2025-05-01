package com._dengz.mungcourse.dto.routine;

import com._dengz.mungcourse.entity.RepeatDay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.List;

@Getter
public class RoutineRequest {

    @NotBlank(message = "루틴 이름은 필수입니다.")
    private String name;

    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "알람 시간은 HH:mm 형식이어야 합니다.")
    private String alarmTime;

    @NotEmpty(message = "반복 요일은 최소 한 개 이상 선택해야 합니다.")
    private List<RepeatDay> repeatDays;
}
