package com._dengz.mungcourse.entity;

import com._dengz.mungcourse.dto.dog.request.DogUpdateRequest;
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

    @OneToMany(mappedBy = "dog", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
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

    public void updateDogInfo(DogUpdateRequest dogUpdateRequest) {
        if (dogUpdateRequest.getName() != null) this.name = dogUpdateRequest.getName();
        if (dogUpdateRequest.getGender() != null) this.gender = dogUpdateRequest.getGender();
        if (dogUpdateRequest.getBreed() != null) this.breed = dogUpdateRequest.getBreed();
        if (dogUpdateRequest.getBirthDate() != null) this.birthDate = dogUpdateRequest.getBirthDate();
        if (dogUpdateRequest.getWeight() != null) this.weight = dogUpdateRequest.getWeight();
        if (dogUpdateRequest.getHasArthritis() != null) this.hasArthritis = dogUpdateRequest.getHasArthritis();
        if (dogUpdateRequest.getNeutered() != null) this.neutered = dogUpdateRequest.getNeutered();
        if (dogUpdateRequest.getDogImgUrl() != null) this.dogImgUrl = dogUpdateRequest.getDogImgUrl();
    }
}