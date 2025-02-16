package com.example.imageannotationapi.services;

import com.example.imageannotationapi.repositories.AnnotationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotationService {
    private final AnnotationRepository annotationRepository;

    public AnnotationService(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    public List<String> getAllAnnotations() {
        return annotationRepository.findAllNames();
    }
}
