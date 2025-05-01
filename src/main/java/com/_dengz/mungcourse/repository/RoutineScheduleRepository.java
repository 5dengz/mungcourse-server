package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.Routine;
import com._dengz.mungcourse.entity.RoutineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoutineScheduleRepository extends JpaRepository<RoutineSchedule, Long> {
    List<RoutineSchedule> findAllByRoutine(Routine routine);
    List<RoutineSchedule> findAllByRoutineAndIsActiveTrue(Routine routine);
    void deleteByRoutine(Routine routine);
}
