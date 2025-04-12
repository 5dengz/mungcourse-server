package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
    boolean existsByUser(User user);
}
