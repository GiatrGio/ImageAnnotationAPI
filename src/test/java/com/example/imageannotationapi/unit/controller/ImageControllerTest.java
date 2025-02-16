package com.example.imageannotationapi.unit.controller;

import com.example.imageannotationapi.controllers.ImageController;
import com.example.imageannotationapi.models.response.ImageDetailsResponse;
import com.example.imageannotationapi.services.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void testUploadImages() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("images", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        mockMvc.perform(multipart("/images/upload")
                        .file(mockFile)
                        .param("userId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());
    }

    @Test
    void testUploadImagesWithInvalidFileType() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "images", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test text content".getBytes());

        mockMvc.perform(multipart("/images/upload")
                        .file(mockFile)
                        .param("userId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetImageStatus() throws Exception {
        when(imageService.getImagesStatus(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/images/status")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetImageStatusWithInvalidUserId() throws Exception {
        mockMvc.perform(get("/images/status")
                        .param("userId", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllImages() throws Exception {
        when(imageService.getAllImagesByUserId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/images/all")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetImageDetails() throws Exception {
        ImageDetailsResponse imageDetailsResponse = new ImageDetailsResponse(
                1L, "status", "url", Collections.emptyList());

        when(imageService.getImageDetails(1L, 1L))
                .thenReturn(imageDetailsResponse);

        mockMvc.perform(get("/images/details")
                        .param("userId", "1")
                        .param("imageId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetImageDetailsWithInvalidUserId() throws Exception {
        mockMvc.perform(get("/images/details")
                        .param("userId", "-1")
                        .param("imageId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetImageDetailsNoImageFound() throws Exception {
        when(imageService.getImageDetails(1L, 1L)).thenReturn(null);

        mockMvc.perform(get("/images/details")
                        .param("userId", "1")
                        .param("imageId", "1"))
                .andExpect(status().isNoContent());
    }

}
