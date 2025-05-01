package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.Routine;
import com._dengz.mungcourse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<Routine> findByIdAndUser(Long routineId, User user);
}
