package com.example.imageannotationapi.models.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageToUpload {
    private Long userId;
    private MultipartFile image;
    private String name;
    private String url;

    public ImageToUpload(Long userId, MultipartFile image) {
        this.userId = userId;
        this.image = image;
        this.name = image.getOriginalFilename();
        this.url = userId + "/" + image.getOriginalFilename();
    }
}
