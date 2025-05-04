package com._dengz.mungcourse.scheduler;

import com._dengz.mungcourse.client.AiClient;
import com._dengz.mungcourse.dto.ai.WalkTrainModelAiRequest;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.entity.Walk;
import com._dengz.mungcourse.repository.UserRepository;
import com._dengz.mungcourse.repository.WalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiModelTrainScheduler {

    private final UserRepository userRepository;
    private final WalkRepository walkRepository;
    private final AiClient aiClient;

    // 72시간마다 실행 (3일마다 새벽 3시)
    @Scheduled(cron = "0 0 3 */3 * *")
    public void scheduleTrainingPerUser() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<Walk> walks = walkRepository.findByUser(user);

            if (walks.isEmpty() || user.getPklFile() == null) {
                continue; // 데이터 없으면 skip
            }

            List<WalkTrainModelAiRequest> requestList = walks.stream()
                    .map(WalkTrainModelAiRequest::create) // gps + label 구조
                    .toList();

            aiClient.sendTrainingData(user, requestList); // 아래 구현
        }
    }
}
