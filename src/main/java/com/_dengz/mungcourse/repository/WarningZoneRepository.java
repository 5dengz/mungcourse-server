package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.WarningZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarningZoneRepository  extends JpaRepository<WarningZone, Long> {
    List<WarningZone> findAllByLatBetweenAndLngBetween(double minLat, double maxLat, double minLng, double maxLng);
}
