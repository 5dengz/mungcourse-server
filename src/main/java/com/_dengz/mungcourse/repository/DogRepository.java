package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
