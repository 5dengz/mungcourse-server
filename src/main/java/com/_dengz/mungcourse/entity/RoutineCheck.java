package com._dengz.mungcourse.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_schedule_id", nullable = false)
    private RoutineSchedule routineSchedule;

    private RoutineCheck(LocalDate date, RoutineSchedule routineSchedule) {
        this.date = date;
        this.routineSchedule = routineSchedule;
    }

    public static RoutineCheck create(LocalDate date, RoutineSchedule routineSchedule) {
        return new RoutineCheck(date, routineSchedule);
    }
}