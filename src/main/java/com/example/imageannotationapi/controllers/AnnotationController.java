package com.example.imageannotationapi.controllers;

import com.example.imageannotationapi.services.AnnotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/annotations")
public class AnnotationController {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationController.class);

    private final AnnotationService annotationService;

    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    @GetMapping(value = "/all")
    @Operation(summary = "Get all available annotations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Unknown error encountered")
    })
    public List<String> getAllAnnotations() {
        logger.info("Request to get all annotations");
        return annotationService.getAllAnnotations();
    }
}
