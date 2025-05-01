package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.Routine;
import com._dengz.mungcourse.entity.RoutineCheck;
import com._dengz.mungcourse.entity.RoutineSchedule;
import com._dengz.mungcourse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineCheckRepository extends JpaRepository<RoutineCheck, Long> {
    List<RoutineCheck> findAllByRoutineSchedule_Routine_UserAndDate(User user, LocalDate date);
    List<RoutineCheck> findAllByRoutineSchedule(RoutineSchedule routineSchedule);
    Optional<RoutineCheck> findByIdAndRoutineSchedule_Routine_User(Long id, User user);
    List<RoutineCheck> findAllByRoutineSchedule_RoutineAndDateGreaterThanEqual(Routine routine, LocalDate fromDate);
}
