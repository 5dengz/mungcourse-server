package com._dengz.mungcourse.service;

import com._dengz.mungcourse.dto.image.ImageDeleteRequest;
import com._dengz.mungcourse.dto.image.ImageRequest;
import com._dengz.mungcourse.dto.image.ImageResponse;
import com._dengz.mungcourse.entity.Dog;
import com._dengz.mungcourse.entity.User;
import com._dengz.mungcourse.exception.DogImageAccessForbiddenException;
import com._dengz.mungcourse.exception.DogImageNotFoundException;
import com._dengz.mungcourse.exception.FileNameExtensionNotFoundException;
import com._dengz.mungcourse.repository.DogRepository;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    private final DogRepository dogRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final int TIME_LIMIT = 1000 * 60; // 1분

    public ImageResponse makePreSignedUrls(ImageRequest imageRequest) {
        //확장자 검사
        checkFileNameExtension(imageRequest.getFileNameExtension());

        String key = UUID.randomUUID() + imageRequest.getFileName();
        String fileNameExtension = imageRequest.getFileNameExtension().toUpperCase();

        //s3 디렉터리 경로 설정
        if (!fileNameExtension.equals("")) {
            key = fileNameExtension + "/" + key;
        }

        String url = getUrl(key);

        return ImageResponse.create(key, getPreSignedUrl(key), url);
    }

    private void checkFileNameExtension(String fileNameExtension) {
        fileNameExtension = fileNameExtension.toUpperCase();

        System.out.println(fileNameExtension);

        if (fileNameExtension.equals("JPG")
                || fileNameExtension.equals("JPEG")
                || fileNameExtension.equals("PNG")) {
            return;
        }

        throw new FileNameExtensionNotFoundException();
    }

    private String getPreSignedUrl(String key) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(key);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String key) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, key)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + TIME_LIMIT;
        expiration.setTime(expTimeMillis);
        return expiration;
    }


    public void deleteImage(ImageDeleteRequest imageDeleteRequest, User user) {
        String key = imageDeleteRequest.getKey();
        validateOwnershipOrThrow(key, user);
        amazonS3.deleteObject(bucket, key);
    }

    public String getUrl(String key) {
        return amazonS3.getUrl(bucket, key).toString();
    }


    public void validateOwnershipOrThrow(String key, User user) {
        String expectedUrlPrefix = "https://mungcourse-s3.s3.ap-northeast-2.amazonaws.com/";

        String fullUrl = expectedUrlPrefix + key;

        System.out.println(key);
        System.out.println(fullUrl);

        Dog dog = dogRepository.findByDogImgUrl(fullUrl)
                .orElseThrow(DogImageNotFoundException::new);

        if (!dog.getUser().getId().equals(user.getId())) {
            throw new DogImageAccessForbiddenException();
        }
    }
}
