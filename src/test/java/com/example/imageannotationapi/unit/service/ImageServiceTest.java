package com.example.imageannotationapi.unit.service;

import com.example.imageannotationapi.repositories.ImageRepository;
import com.example.imageannotationapi.services.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetImagesStatus() {
        when(imageRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());

        assertEquals(0, imageService.getImagesStatus(1L).size());
    }

    @Test
    void testGetAllImagesByUserId() {
        when(imageRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());

        assertEquals(0, imageService.getAllImagesByUserId(1L).size());
    }

    @Test
    void testGetImageDetails() {
        when(imageRepository.findImageDetailsByUserIdAndImageId(1L, 1L)).thenReturn(Optional.empty());

        assertNull(imageService.getImageDetails(1L, 1L));
    }
}
