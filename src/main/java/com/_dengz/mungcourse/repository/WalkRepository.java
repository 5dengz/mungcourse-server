package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalkRepository extends JpaRepository<Walk, Long> {
    List<Walk> findAllByUserAndStartedAtBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Walk> findByUser(User user);
    Optional<Walk> findTopByUserOrderByStartedAtDesc(User user);
}
