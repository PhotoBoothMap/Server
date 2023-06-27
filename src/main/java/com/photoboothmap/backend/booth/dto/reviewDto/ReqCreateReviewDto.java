package com.photoboothmap.backend.booth.dto.reviewDto;

import com.photoboothmap.backend.booth.utils.ReviewUtils;
import com.photoboothmap.backend.util.entity.TagType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateReviewDto {
    private float starRate;
    private Optional<String> content;
    private List<TagType> userTags;

    private void setUserTags(List<String> userTags){
        this.userTags = ReviewUtils.convertStringToTagEnum(userTags);
    }

}
