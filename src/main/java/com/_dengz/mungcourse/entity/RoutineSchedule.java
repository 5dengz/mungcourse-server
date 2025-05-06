package com._dengz.mungcourse.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RepeatDay repeatDay;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @OneToMany(mappedBy = "routineSchedule", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RoutineCheck> checks;

    private RoutineSchedule(RepeatDay dayOfWeek, Routine routine) {
        this.repeatDay = dayOfWeek;
        this.routine = routine;
    }

    public static RoutineSchedule create(RepeatDay dayOfWeek, Routine routine) {
        return new RoutineSchedule(dayOfWeek, routine);
    }
}
