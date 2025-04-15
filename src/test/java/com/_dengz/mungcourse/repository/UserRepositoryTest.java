package com._dengz.mungcourse.repository;

import com._dengz.mungcourse.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 저장 후 조회")
    @Test
    void saveAndFind() {
        // given
        User user = User.create("103", "user@gmail.com", "홍길동", "google");
        // when
        User savedUser = userRepository.save(user);
        User foundedUser = userRepository.findById(savedUser.getId()).orElse(null);

        // then
        Assertions.assertThat(foundedUser).isNotNull();
        Assertions.assertThat(foundedUser.getName()).isEqualTo("홍길동");
    }

    @DisplayName("유저 이메일 값 unique 제약조건 조회")
    @Test
    void uniqueConstraint() {
        // given
        User user1 = User.create("103", "user@gmail.com", "홍길동", "google");
        User user2 = User.create("105", "user@gmail.com", "김철수", "google");

        // when
        userRepository.save(user1);
        userRepository.flush(); // 1차 캐시가 아닌 DB에 반영

        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
            userRepository.flush(); // 예외 발생 지점
        });
    }
}