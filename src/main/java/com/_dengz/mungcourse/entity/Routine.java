package com._dengz.mungcourse.entity;

import com._dengz.mungcourse.dto.dog.request.DogUpdateRequest;
import com._dengz.mungcourse.dto.routine.RoutineUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "routines") // SQL에서 예약어 충돌 방지
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String alarmTime; // time 자료형은 보통 String 또는 LocalTime 사용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RoutineSchedule> schedules;

    private Routine(String name, String alarmTime, User user) {
        this.name = name;
        this.alarmTime = alarmTime;
        this.user = user;
    }

    public static Routine create(String name, String alarmTime, User user) {
        return new Routine(name, alarmTime, user);
    }

    public void updateRoutineInfo(RoutineUpdateRequest routineUpdateRequest) {
        if (routineUpdateRequest.getName() != null) this.name = routineUpdateRequest.getName();
        if (routineUpdateRequest.getAlarmTime() != null) this.alarmTime = routineUpdateRequest.getAlarmTime();
    }
}