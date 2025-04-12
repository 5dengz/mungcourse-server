package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.dog.DogListResponse;
import com._dengz.mungcourse.dto.dog.DogRequest;
import com._dengz.mungcourse.dto.dog.DogResponse;
import com._dengz.mungcourse.dto.dog.MainDogResponse;
import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.DogAccessForbiddenException;
import com._dengz.mungcourse.exception.DogListNotFoundException;
import com._dengz.mungcourse.exception.DogNotFoundException;
import com._dengz.mungcourse.exception.MainDogNotFoundException;
import com._dengz.mungcourse.jwt.TokenProvider;
import com._dengz.mungcourse.repository.DogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogService {
    private final TokenProvider tokenProvider;
    private final DogRepository dogRepository;
    private final AuthService authService;

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

    public MainDogResponse searchMainDog(User user) {

        Dog mainDog = dogRepository.findByUserAndIsMainTrue(user)
                .orElseThrow(MainDogNotFoundException::new);

        return MainDogResponse.create(mainDog);
    }

    public DogResponse searchDogDetail(Long id, User user) {

        Dog dog = dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new); // 강아지가 아예 존재하지 않을 때

        if (!dog.getUser().getId().equals(user.getId())) {
            throw new DogAccessForbiddenException(); // 유저가 접근 권한 없을 때
        }

        return DogResponse.create(dog);
    }
}
