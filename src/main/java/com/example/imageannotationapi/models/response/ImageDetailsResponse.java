package com.example.imageannotationapi.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDetailsResponse {
    Long imageId;
    String status;
    String url;
    List<String> annotations;
}
