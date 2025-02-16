package com.example.imageannotationapi.integration.service;

import com.example.imageannotationapi.models.database.Annotation;
import com.example.imageannotationapi.models.database.Image;
import com.example.imageannotationapi.models.input.ImageToUpload;
import com.example.imageannotationapi.repositories.AnnotationRepository;
import com.example.imageannotationapi.repositories.ImageRepository;
import com.example.imageannotationapi.repositories.UserRepository;
import com.example.imageannotationapi.services.ImageService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ImageServiceIT {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Test
    @Transactional
    void testPersistImageAndImageMetadataToDatabases() {
        Long userId = 1L;
        ImageToUpload imageToUpload = createFakeImageToUploadObject();

        Long imageId = imageService.persistImageAndImageMetadataToDatabases(imageToUpload);

        assertThat(imageId).isNotNull();
        assertThat(imageRepository.existsById(imageId)).isTrue();
        assertThat(userRepository.existsById(userId)).isTrue();
    }

    @Test
    @Transactional
    void testGenerateAndAddAnnotationsToImage() {
        ImageToUpload imageToUpload = createFakeImageToUploadObject();
        Long imageId = imageService.persistImageAndImageMetadataToDatabases(imageToUpload);

        imageService.generateAndAddAnnotationsToImage(imageToUpload, imageId);

        Image image = imageRepository.findById(imageId).orElseThrow();
        assertThat(image.getStatus()).isEqualTo("PROCESSING");
        assertThat(image.getAnnotations()).isEmpty();

        Set<Annotation> annotations = image.getAnnotations();
        assertThat(annotations).isEmpty();
    }

    private static ImageToUpload createFakeImageToUploadObject() {
        Long userId = 1L;
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image", "testImage.jpg", "image/jpeg", "test image content".getBytes());
        return new ImageToUpload(userId, mockMultipartFile);
    }
}
