package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.routine.RoutinePostResponse;
import com._dengz.mungcourse.dto.routine.RoutineRequest;
import com._dengz.mungcourse.entity.*;
import com._dengz.mungcourse.repository.RoutineCheckRepository;
import com._dengz.mungcourse.repository.RoutineRepository;
import com._dengz.mungcourse.repository.RoutineScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineScheduleRepository routineScheduleRepository;
    private final RoutineCheckRepository routineCheckRepository;

    @Transactional
    public RoutinePostResponse makeRoutine(RoutineRequest routineRequest, User user) {

        Routine routine = Routine.create(routineRequest.getName(), routineRequest.getAlarmTime(), user);

        routineRepository.save(routine); // 루틴 정보부터 저장

        List<RoutineSchedule> routineSchedules = new ArrayList<>();
        Map<RepeatDay, RoutineSchedule> scheduleMap = new HashMap<>();
        routineRequest.getRepeatDays().forEach(repeatDay -> {
            RoutineSchedule routineSchedule = RoutineSchedule.create(repeatDay, routine);
            routineSchedules.add(routineSchedule); // response로 넘겨주는 용도
            routineScheduleRepository.save(routineSchedule); // 해당 루틴의 요일들 저장
            scheduleMap.put(repeatDay, routineSchedule);
        });

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1); // 오늘 이후로 1년까지 루틴 요일에 맞는 날짜들 저장

        System.out.println(today);
        System.out.println(endDate);

        while (!today.isAfter(endDate)) {
            RepeatDay currentRepeatDay = RepeatDay.fromJavaDayOfWeek(today.getDayOfWeek()); // today의 요일과 RepeatDay 클래스 자료형이 다르기때문에 변환

            if (scheduleMap.containsKey(currentRepeatDay)) { // 만약 해당 날짜가 지정한 요일에 맞는 날짜면
                RoutineSchedule routineSchedule = scheduleMap.get(currentRepeatDay);
                RoutineCheck routineCheck = RoutineCheck.create(today, routineSchedule); // 요일에 맞는 routineScedule 조회해서 같이 저장
                routineCheckRepository.save(routineCheck);
            }
            today = today.plusDays(1);
        }


        return RoutinePostResponse.create(routine, routineSchedules);
    }
}
