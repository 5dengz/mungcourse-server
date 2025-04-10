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
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dogImgUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;


    private LocalDate birthday;
    private String breed;

    private Float weight;

    @Column(nullable = false)
    private Boolean hasArthritis = false;

    @Column(nullable = false)
    private Boolean neutered = false;

    @Column(nullable = false)
    private Boolean isMain = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "dog", fetch = FetchType.LAZY)
    private List<WalkDog> walkDogs;
}