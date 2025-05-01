package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.routine.*;
import com._dengz.mungcourse.exception.RoutineNotFoundException;
import com._dengz.mungcourse.exception.RoutineCheckNotFoundException;
import com._dengz.mungcourse.entity.*;
import com._dengz.mungcourse.repository.RoutineCheckRepository;
import com._dengz.mungcourse.repository.RoutineRepository;
import com._dengz.mungcourse.repository.RoutineScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineScheduleRepository routineScheduleRepository;
    private final RoutineCheckRepository routineCheckRepository;

    public List<RoutineResponse> findRoutineByDate(LocalDate date, User user) {
        List<RoutineCheck> routineChecks = routineCheckRepository.findAllByRoutineSchedule_Routine_UserAndDate(user, date);

        return routineChecks.stream()
                .map(routineCheck -> {
                    RoutineSchedule routineSchedule = routineCheck.getRoutineSchedule();
                    Routine routine = routineSchedule.getRoutine();
                    return RoutineResponse.create(routineCheck, routine);
                })
                .collect(Collectors.toList());
    }


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
        }); // 루틴 스케줄도 저장

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1); // 오늘 이후로 1년까지 루틴 요일에 맞는 날짜들 저장

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

    public RoutinePostResponse findRoutineDetail(Long routineId, User user) {
        Routine routine = routineRepository.findByIdAndUser(routineId, user)
                .orElseThrow(RoutineNotFoundException::new);

        List<RoutineSchedule> routineSchedules = routineScheduleRepository.findAllByRoutineAndIsActiveTrue(routine);

        return RoutinePostResponse.create(routine, routineSchedules);
    }

    @Transactional
    public RoutinePostResponse updateRoutine(Long routineId, RoutineUpdateRequest routineUpdateRequest, User user) {
        Routine routine = routineRepository.findByIdAndUser(routineId, user)
                .orElseThrow(RoutineNotFoundException::new);

        routine.updateRoutineInfo(routineUpdateRequest); // 이름, 알람 시간 등 일반 정보는 수정

        LocalDate applyFromDate = routineUpdateRequest.getApplyFromDate();

        List<RoutineCheck> checksToDelete = routineCheckRepository
                .findAllByRoutineSchedule_RoutineAndDateGreaterThanEqual(routine, applyFromDate); // applyFromDate 이후의 루틴 체크 삭제

        // 기존 스케줄 비활성화
        List<RoutineSchedule> existingSchedules = routineScheduleRepository.findAllByRoutine(routine);
        existingSchedules.forEach(schedule -> {
            schedule.setIsActive(false);
            routineScheduleRepository.save(schedule);
        });

        routineCheckRepository.deleteAll(checksToDelete);

        List<RoutineSchedule> newRoutineSchedules = new ArrayList<>();
        Map<RepeatDay, RoutineSchedule> scheduleMap = new HashMap<>();

        routineUpdateRequest.getRepeatDays().forEach(repeatDay -> {
            RoutineSchedule routineSchedule = RoutineSchedule.create(repeatDay, routine);
            newRoutineSchedules.add(routineSchedule); // response로 넘겨주는 용도
            routineScheduleRepository.save(routineSchedule); // 해당 루틴의 요일들 저장
            scheduleMap.put(repeatDay, routineSchedule);
        }); // 루틴 스케줄도 저장


        LocalDate endDate = applyFromDate.plusYears(1); // 오늘 이후로 1년까지 루틴 요일에 맞는 날짜들 저장

        while (!applyFromDate.isAfter(endDate)) {
            RepeatDay currentRepeatDay = RepeatDay.fromJavaDayOfWeek(applyFromDate.getDayOfWeek()); // today의 요일과 RepeatDay 클래스 자료형이 다르기때문에 변환

            if (scheduleMap.containsKey(currentRepeatDay)) { // 만약 해당 날짜가 지정한 요일에 맞는 날짜면
                RoutineSchedule routineSchedule = scheduleMap.get(currentRepeatDay);
                RoutineCheck routineCheck = RoutineCheck.create(applyFromDate, routineSchedule); // 요일에 맞는 routineScedule 조회해서 같이 저장
                routineCheckRepository.save(routineCheck);
            }
            applyFromDate = applyFromDate.plusDays(1);
        }

        return RoutinePostResponse.create(routine, newRoutineSchedules);
    }

    @Transactional
    public void deleteRoutine(Long routineId, User user) {

        Routine routine = routineRepository.findByIdAndUser(routineId, user)
                .orElseThrow(RoutineNotFoundException::new);

        List<RoutineSchedule> schedules = routineScheduleRepository.findAllByRoutine(routine);

        schedules.forEach(schedule -> {
            List<RoutineCheck> checks = routineCheckRepository.findAllByRoutineSchedule(schedule);
            routineCheckRepository.deleteAll(checks);
        }); // routineSchedule과 routineCheck은 CASCADE.REMOVE 관계가 아니기 때문에 직접 삭제해줘야함

        routineRepository.delete(routine);

    }

    @Transactional
    public RoutineCheckResponse toggleRoutine(Long routineCheckId, User user) {
        RoutineCheck routineCheck = routineCheckRepository.findByIdAndRoutineSchedule_Routine_User(routineCheckId, user)
                .orElseThrow(RoutineCheckNotFoundException::new);

        routineCheck.setIsCompleted(!routineCheck.getIsCompleted());
        return RoutineCheckResponse.create(routineCheckRepository.save(routineCheck));
    }
}
