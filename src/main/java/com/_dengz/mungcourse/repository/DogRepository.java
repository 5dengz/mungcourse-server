package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog, Long> {
    boolean existsByUser(User user);
    Optional<Dog> findByUserAndIsMainTrue(User user);
    List<Dog> findAllByUser(User user);
    Optional<Dog> findFirstByUserOrderByPostedAtAsc(User user);
    Optional<Dog> findByDogImgUrl(String url);
}
