package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.DogPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogPlaceRepository extends JpaRepository<DogPlace, Long> {
    List<DogPlace> findAllByLatBetweenAndLngBetween(double minLat, double maxLat, double minLng, double maxLng);
}
