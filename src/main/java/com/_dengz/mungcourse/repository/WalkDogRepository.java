package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.Walk;
import com._dengz.mungcourse.entity.WalkDog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkDogRepository extends JpaRepository<WalkDog, Long> {
    List<WalkDog> findAllByWalk(Walk walk);
    List<WalkDog> findAllByDog(Dog dog);
}
