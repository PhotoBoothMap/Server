package com.photoboothmap.backend.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ReviewListDto {
    private String user;

    private String content;

    private Float score;

    private String brand;

    private String name;

    private List<byte[]> imgFile;

    private List<String> userTags;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime date;
}
