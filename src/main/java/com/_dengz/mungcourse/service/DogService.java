package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.dog.DogRequest;
import com._dengz.mungcourse.dto.dog.DogResponse;
import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.repository.DogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DogService {
    private final DogRepository dogRepository;
    private final AuthService authService;

    public DogResponse makeDog(HttpServletRequest request, DogRequest dogRequest) {

        User user = authService.getCurrentUser(request);

        boolean isFirstDog = !dogRepository.existsByUser(user); // 처음 등록한 강아지면 자동으로 isMain = true로 해줌

        Dog dog = Dog.create(dogRequest.getName(), dogRequest.getGender(), dogRequest.getBirthDate(), dogRequest.getBreed(),
                dogRequest.getWeight(), dogRequest.getPostedAt(), dogRequest.getHasArthritis(), dogRequest.getNeutered(), dogRequest.getDogImgUrl(), isFirstDog, user);

        return DogResponse.create(dogRepository.save(dog));

    }
}
