package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.DogPlace;
import com._dengz.mungcourse.entity.SmokingZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmokingZoneRepository  extends JpaRepository<SmokingZone, Long> {
    List<SmokingZone> findAllByLatBetweenAndLngBetween(double minLat, double maxLat, double minLng, double maxLng);
}
