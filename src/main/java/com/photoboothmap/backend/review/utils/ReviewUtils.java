package com.photoboothmap.backend.review.utils;

import com.photoboothmap.backend.review.dto.ReviewListDto;
import com.photoboothmap.backend.review.entity.ReviewEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewUtils {

    public static List<ReviewListDto> toReviewListDto(List<ReviewEntity> entityList) {
        return entityList.stream()
                .map(r -> ReviewListDto.builder()
                        .user(r.getMember().getNickname())
                        .score(r.getStarRate())
                        .content(r.getContent())
                        .userTags(r.getTags().stream()
                                .map(entity -> entity.getTag().getTag())
                                .collect(Collectors.toList()))
                        .imgUrl(r.getImageUrls().stream()
                                .map(entity -> entity.getImgUrl())
                                .collect(Collectors.toList()))
                        .date(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}