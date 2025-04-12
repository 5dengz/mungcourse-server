package com._dengz.mungcourse.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    private LocalDate birthDate;

    private String breed;

    private Float weight;

    @Column(nullable = false)
    private LocalDateTime postedAt;

    @Column(nullable = false)
    private Boolean hasArthritis = false;

    @Column(nullable = false)
    private Boolean neutered = false;

    private String dogImgUrl;

    @Column(nullable = false)
    private Boolean isMain = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "dog", fetch = FetchType.LAZY)
    private List<WalkDog> walkDogs;

    private Dog(String name, String gender, LocalDate birthDate, String breed, Float weight, LocalDateTime postedAt,
                Boolean hasArthritis, Boolean neutered, String dogImgUrl, Boolean isMain, User user) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.breed = breed;
        this.weight = weight;
        this.postedAt = postedAt;
        this.hasArthritis = hasArthritis;
        this.neutered = neutered;
        this.dogImgUrl = dogImgUrl;
        this.isMain = isMain;
        this.user = user;
    }

    public static Dog create(String name, String gender, LocalDate birthDate, String breed, Float weight, LocalDateTime postedAt,
                             Boolean hasArthritis, Boolean neutered, String dogImgUrl, Boolean isMain, User user) {
        return new Dog(name, gender, birthDate, breed, weight, postedAt, hasArthritis, neutered, dogImgUrl, isMain, user);
    }
}