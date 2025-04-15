package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.dog.request.DogRequest;
import com._dengz.mungcourse.dto.dog.request.DogUpdateRequest;
import com._dengz.mungcourse.dto.dog.response.DogListResponse;
import com._dengz.mungcourse.dto.dog.response.DogResponse;
import com._dengz.mungcourse.dto.dog.response.MainDogResponse;
import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.DogAccessForbiddenException;
import com._dengz.mungcourse.exception.DogListNotFoundException;
import com._dengz.mungcourse.exception.DogNotFoundException;
import com._dengz.mungcourse.exception.MainDogNotFoundException;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogService {
    private final DogRepository dogRepository;

    @Transactional(readOnly = true)
    public List<DogListResponse> searchAllDogs(User user) {

        List<Dog> dogs = dogRepository.findAllByUser(user);


        if (dogs.isEmpty()) {
            throw new DogListNotFoundException();
        }

        return dogs.stream()
                .map(DogListResponse::create)
                .collect(Collectors.toList());
    }

    public DogResponse makeDog(DogRequest dogRequest, User user) {

        boolean isFirstDog = !dogRepository.existsByUser(user); // 처음 등록한 강아지면 자동으로 isMain = true로 해줌

        Dog dog = Dog.create(dogRequest.getName(), dogRequest.getGender(), dogRequest.getBirthDate(), dogRequest.getBreed(),
                dogRequest.getWeight(), dogRequest.getPostedAt(), dogRequest.getHasArthritis(), dogRequest.getNeutered(), dogRequest.getDogImgUrl(), isFirstDog, user);

        return DogResponse.create(dogRepository.save(dog));

    }

    @Transactional(readOnly = true)
    public MainDogResponse searchMainDog(User user) {

        Dog mainDog = dogRepository.findByUserAndIsMainTrue(user)
                .orElseThrow(MainDogNotFoundException::new);

        return MainDogResponse.create(mainDog);
    }

    @Transactional(readOnly = true)
    public DogResponse searchDogDetail(Long id, User user) {

        Dog dog = findAndCheckDogById(id, user);

        return DogResponse.create(dog);
    }

    @Transactional
    public DogResponse updateDog(Long id, DogUpdateRequest dogUpdateRequest, User user) {

        Dog dog = findAndCheckDogById(id, user);

        dog.updateDogInfo(dogUpdateRequest);

        return DogResponse.create(dog);
    }

    @Transactional
    public void deleteDog(Long id, User user) {
        Dog dog = findAndCheckDogById(id, user);

        boolean wasMain = dog.getIsMain();

        dogRepository.delete(dog);

        // 삭제한 강아지가 메인이었으면, 가장 오래된 강아지를 메인으로 설정
        if (wasMain) {
            dogRepository.findFirstByUserOrderByPostedAtAsc(user)
                    .ifPresent(oldestDog -> oldestDog.setIsMain(true));
        }
    }

    @Transactional
    public DogResponse setMainDog(Long id, User user) {
        // 1. 현재 메인 강아지 → isMain = false
        dogRepository.findByUserAndIsMainTrue(user)
                .ifPresent(dog -> dog.setIsMain(false));

        // 2. 바꾸려는 강아지 찾기
        Dog newMainDog = dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new);

        // 3. 유저 검증
        if (!newMainDog.getUser().getId().equals(user.getId())) {
            throw new DogAccessForbiddenException();
        }

        // 4. 해당 강아지를 메인으로 설정
        newMainDog.setIsMain(true);

        // 5. 응답
        return DogResponse.create(newMainDog);
    }

    public Dog findAndCheckDogById(Long id, User user) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new); // 강아지가 아예 존재하지 않을 때

        if (!dog.getUser().getId().equals(user.getId())) {
            throw new DogAccessForbiddenException(); // 유저가 접근 권한 없을 때
        }

        return dog;
    }
}
