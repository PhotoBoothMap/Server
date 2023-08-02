package com.photoboothmap.backend.booth.dto.reviewDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveImageRes {
    private String imageUrl;
    private byte[] imageFile;
}
