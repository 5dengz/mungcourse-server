package com._dengz.mungcourse.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DogPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private Double lat;

    private Double lng;

    @Column(nullable = true)
    private String phoneNum;

    private String closedDays;

    private String openingHours;

    @Column(nullable = true)
    private String placeImgUrl;

    private DogPlace(String name, String category, Double lat, Double lng, String phoneNum, String closedDays, String openingHours, String placeImgUrl){
        this.name = name;
        this.category = category;
        this.lat = lat;
        this.lng = lng;
        this.phoneNum = phoneNum;
        this.closedDays = closedDays;
        this.openingHours = openingHours;
        this.placeImgUrl = placeImgUrl;
    }
}
