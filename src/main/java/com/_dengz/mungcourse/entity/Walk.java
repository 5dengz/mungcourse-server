package com._dengz.mungcourse.entity;

import com._dengz.mungcourse.dto.walk.WalkRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Walk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float distanceKm;

    @Column(nullable = false)
    private Integer durationSec;

    @Column(nullable = false)
    private Integer calories;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String gpsData;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "walk", fetch = FetchType.LAZY)
    private List<WalkDog> walkDogs;

    private Walk(Float distanceKm, Integer durationSec, Integer calories, String gpsData,
                         LocalDateTime startedAt, LocalDateTime endedAt, User user)
    {
        this.distanceKm = distanceKm;
        this.durationSec = durationSec;
        this.calories = calories;
        this.gpsData = gpsData;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.user = user;
    }

    public static Walk create(Float distanceKm, Integer durationSec, Integer calories, String gpsData,
                       LocalDateTime startedAt, LocalDateTime endedAt, User user)
    {
        return new Walk(distanceKm, durationSec, calories, gpsData, startedAt, endedAt, user);
    }
}