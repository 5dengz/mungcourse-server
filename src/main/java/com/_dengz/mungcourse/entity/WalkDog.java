package com._dengz.mungcourse.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkDog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_id", nullable = false)
    private Walk walk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

    private WalkDog(Walk walk, Dog dog) {
        this.walk = walk;
        this.dog = dog;
    }

    public static WalkDog create(Walk walk, Dog dog) {
        return new WalkDog(walk, dog);
    }
}