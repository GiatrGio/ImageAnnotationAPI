package com.example.imageannotationapi.services;

import com.example.imageannotationapi.models.input.ImageToUpload;
import com.example.imageannotationapi.models.database.Annotation;
import com.example.imageannotationapi.models.database.Image;
import com.example.imageannotationapi.models.database.User;
import com.example.imageannotationapi.models.response.ImageDetailsResponse;
import com.example.imageannotationapi.models.response.ImageStatusResponse;
import com.example.imageannotationapi.repositories.AnnotationRepository;
import com.example.imageannotationapi.repositories.ImageRepository;
import com.example.imageannotationapi.repositories.UserRepository;
import com.example.imageannotationapi.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final AnnotationRepository annotationRepository;

    public ImageService(ImageRepository imageRepository, UserRepository userRepository, AnnotationRepository annotationRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.annotationRepository = annotationRepository;
    }

    @Transactional
    public Long persistImageAndImageMetadataToDatabases(ImageToUpload imageToUpload) {
        logger.info("Persisting image and metadata to databases");
        saveImageToS3(imageToUpload);
        return addImageMetadataToMySQL(imageToUpload);
    }

    @Async
    public void generateAndAddAnnotationsToImage(ImageToUpload imageToUpload, Long imageId) {
        logger.info("Generating and adding annotations to image");
        List<String> annotations = generateRandomAnnotationsForImage(imageToUpload);

        if (annotations.isEmpty()) {
            updateImageStatus(imageId, "FAIL");
        }
        else {
            addImageAnnotations(annotations, imageId);
            updateImageStatus(imageId, "SUCCESS");
        }
    }

    private List<String> generateRandomAnnotationsForImage(ImageToUpload image) {
        String[] randomAnnotations = {"mountain", "sea", "chair", "dog", "fire", "jar", "tree", "sky", "cloud", "car"};
        List<String> annotationsList = Arrays.asList(randomAnnotations);
        Collections.shuffle(annotationsList);

        Utils.timeoutToSimulateLongProcess();

        return annotationsList.subList(0, 3);
    }

    private void saveImageToS3(ImageToUpload imageToUpload) {
        // Simulate saving image to S3
    }

    public Long addImageMetadataToMySQL(ImageToUpload imageToUpload) {

        userRepository.findById(imageToUpload.getUserId()).orElseGet(() -> {
            User newUser = new User(imageToUpload.getUserId());
            return userRepository.save(newUser);
        });

        imageRepository.addNewImage(imageToUpload.getUserId(), imageToUpload.getUrl());
        return imageRepository.getLastInsertedId();
    }

    public void addImageAnnotations(List<String> annotations, Long imageId) {

        for (String annotationName : annotations) {
            Annotation annotation = annotationRepository.findByName(annotationName)
                    .orElseGet(() -> {
                        Annotation newAnnotation = new Annotation();
                        newAnnotation.setName(annotationName);
                        return annotationRepository.save(newAnnotation);
                    });
            Image image = imageRepository.findByImageId(imageId).orElseThrow();
            image.getAnnotations().add(annotation);

            imageRepository.save(image);
        }
    }

    private void updateImageStatus(Long imageId, String status) {
        Image image = imageRepository.findById(imageId).orElseThrow();
        image.setStatus(status);
        imageRepository.save(image);
    }

    public List<ImageStatusResponse> getImagesStatus(Long userId) {
        List<Image> images = imageRepository.findAllByUserId(userId);

        return images.stream()
                .map(image ->
                        new ImageStatusResponse(image.getId(), image.getStatus()))
                .toList();
    }

    public List<String> getAllImagesByUserId(Long userId) {
        List<Image> images = imageRepository.findAllByUserId(userId);

        return images.stream()
                .map(Image::getUrl)
                .toList();
    }

    public ImageDetailsResponse getImageDetails(Long userId, Long imageId) {
        Image image = imageRepository.findImageDetailsByUserIdAndImageId(userId, imageId)
                .orElse(null);

        if (Objects.isNull(image)) {
            return null;
        }

        return new ImageDetailsResponse(image.getId(), image.getStatus(), image.getUrl(),
                image.getAnnotations().stream()
                        .map(Annotation::getName)
                        .toList());
    }

}
