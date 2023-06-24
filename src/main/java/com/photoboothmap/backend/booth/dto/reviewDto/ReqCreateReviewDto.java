package com.photoboothmap.backend.booth.dto.reviewDto;

import com.photoboothmap.backend.booth.utils.ReviewUtils;
import com.photoboothmap.backend.util.entity.TagType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateReviewDto {
//    private Long boothId;
//    private MultipartFile[] files;
//    private float starRate;
    List<TagType> userTags;

    private void setUserTags(List<String> userTags){
        this.userTags = ReviewUtils.convertStringToTagEnum(userTags);
    }

}
