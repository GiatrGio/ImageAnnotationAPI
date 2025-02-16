package com.example.imageannotationapi.controllers;

import com.example.imageannotationapi.models.input.ImageToUpload;
import com.example.imageannotationapi.models.response.ImageDetailsResponse;
import com.example.imageannotationapi.models.response.ImageStatusResponse;
import com.example.imageannotationapi.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("images")
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload and annotate JPG images")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Images are being uploaded"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Unknown error encountered")
    })
    public ResponseEntity<String> uploadImages(
            @RequestParam("userId") Long userId,
            @RequestParam("images") List<MultipartFile> images) {
        logger.info("Upload images for user with id: {}", userId);

        if (areParametersInvalid(userId, images)) {
            return ResponseEntity.badRequest().build();
        }

        for (MultipartFile image : images) {
            if (isImageInvalid(image)) {
                return ResponseEntity.badRequest().body("Only JPG images are allowed");
            }
        }

        images.forEach(image -> {
            ImageToUpload imageToUpload = new ImageToUpload(userId, image);
            Long imageId = imageService.persistImageAndImageMetadataToDatabases(imageToUpload);
            imageService.generateAndAddAnnotationsToImage(imageToUpload, imageId);
        });

        logger.info("Images are being uploaded");
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "status")
    @Operation(summary = "Get the process status for each uploaded image by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Unknown error encountered")
    })
    public ResponseEntity<List<ImageStatusResponse>> getImageStatus(
            @RequestParam("userId") Long userId) {
        logger.info("Get images status for user with id: {}", userId);
        if (isIdInvalid(userId)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(imageService.getImagesStatus(userId));
    }

    @GetMapping(value = "all")
    @Operation(summary = "Get all images for a user by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Unknown error encountered")
    })
    public ResponseEntity<List<String>> getAllImages(
            @RequestParam("userId") Long userId) {
        logger.info("Get all images for user with id: {}", userId);
        if (isIdInvalid(userId)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(imageService.getAllImagesByUserId(userId));
    }

    @GetMapping(value = "/details")
    @Operation(summary = "Get the details of a specific image by image id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Unknown error encountered")
    })
    public ResponseEntity<ImageDetailsResponse> getImageDetails(
            @RequestParam("userId") Long userId,
            @RequestParam("imageId") Long imageId) {
        logger.info("Get image details for user with id: {} and image id: {}", userId, imageId);
        if (areIdsInvalid(Arrays.asList(userId, imageId))) {
            return ResponseEntity.badRequest().build();
        }

        ImageDetailsResponse imageDetails = imageService.getImageDetails(userId, imageId);
        if (Objects.isNull(imageDetails)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(imageService.getImageDetails(userId, imageId));
    }

    private static boolean isImageInvalid(MultipartFile image) {
        return !image.getContentType().startsWith("image/jpeg");
    }

    private static boolean isIdInvalid(Long id) {
        return Objects.isNull(id) || id <= 0;
    }

    private static boolean areIdsInvalid(List<Long> ids) {
        return Objects.isNull(ids) || ids.isEmpty() || ids.stream().anyMatch(id -> id <= 0);
    }

    private static <T> boolean areParametersInvalid(Long userId, List<T> listInput) {
        return Objects.isNull(userId) || userId <= 0 || Objects.isNull(listInput) || listInput.isEmpty();
    }
}