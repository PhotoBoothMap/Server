package com.photoboothmap.backend.review.utils;

import com.photoboothmap.backend.review.dto.ReviewListDto;
import com.photoboothmap.backend.review.entity.ReviewEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewUtils {

    public static List<ReviewListDto> toReviewListDto(List<ReviewEntity> entityList) {
        return entityList.stream()
                .map(r -> ReviewListDto.builder()
                        .user(r.getMember().getNickname())
                        .score(r.getStarRate())
                        .content(r.getContentOrNull())
                        .brand(r.getPhotoBooth().getBrand().getName())
                        .name(r.getPhotoBooth().getName())
                        .userTags(r.getTags().stream()
                                .map(entity -> entity.getTag().getTag())
                                .collect(Collectors.toList()))
                        .imgFile(r.getImageUrls().stream()
                                .map(entity -> ImageUtils.convertUrlToBinary(entity.getImgUrl()))
                                .collect(Collectors.toList()))
                        .date(r.getCreatedAt().plusHours(9))
                        .build())
                .collect(Collectors.toList());
    }
}