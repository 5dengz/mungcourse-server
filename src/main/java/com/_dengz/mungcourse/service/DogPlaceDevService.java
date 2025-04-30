package com._dengz.mungcourse.service;

import com._dengz.mungcourse.entity.DogPlace;
import com._dengz.mungcourse.exception.DogPlaceNotFoundException;
import com._dengz.mungcourse.properties.GooglePlaceApiProperties;
import com._dengz.mungcourse.repository.DogPlaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class DogPlaceDevService {

    private final DogPlaceRepository dogPlaceRepository;
    private final GooglePlaceApiProperties googlePlaceApiProperties;

    @Transactional
    public void saveDogImageUrl() {
        Long id = 1L;

        String apiKey = googlePlaceApiProperties.getPlaceApiKey();

        while (id < 500) {

            DogPlace dogPlace = dogPlaceRepository.findById(id)
                    .orElseThrow(DogPlaceNotFoundException::new);

            String dogPlaceName = dogPlace.getName();
            String dogPlaceId = findDogPlaceId(dogPlaceName, apiKey);

            if (dogPlaceId == null) {
                id++; // 다음 ID로 넘어감
                continue;
            }

            String dogPlacePhotoReference = findPhotoReference(dogPlaceId, apiKey);

            if (dogPlacePhotoReference == null) {
                id++; // 다음 ID로 넘어감
                continue;
            }

            String imageUrl = findImageUrl(dogPlacePhotoReference, apiKey);

            dogPlace.setPlaceImgUrl(imageUrl);

            dogPlaceRepository.save(dogPlace);

            id++;
        }
    }

    public static String findDogPlaceId(String dogPlaceName, String apiKey) {

        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/json")
                .queryParam("input", dogPlaceName)
                .queryParam("inputtype", "textquery")
                .queryParam("fields", "place_id")
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                return candidates.get(0).path("place_id").asText();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String findPhotoReference(String dogPlaceId, String apiKey) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/details/json")
                .queryParam("place_id", dogPlaceId)
                .queryParam("fields", "photos")
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            JsonNode photos = root.path("result").path("photos");
            if (photos.isArray() && photos.size() > 0) {
                return photos.get(0).path("photo_reference").asText();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String findImageUrl(String photoReference, String apiKey) {
        try {
            String requestUrl = "https://maps.googleapis.com/maps/api/place/photo"
                    + "?maxwidth=400"
                    + "&photoreference=" + photoReference
                    + "&key=" + apiKey;

            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false); // ✅ 자동 리다이렉트 방지
            connection.connect();

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM) {
                // ✅ 302 or 301 응답 → Location 헤더에서 최종 이미지 URL 얻기
                return connection.getHeaderField("Location");
            } else {
                throw new RuntimeException("Expected redirect but got HTTP " + status);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch redirected image URL", e);
        }

    }
}
