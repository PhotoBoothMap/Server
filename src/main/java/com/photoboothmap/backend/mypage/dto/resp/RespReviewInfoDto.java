package com.photoboothmap.backend.mypage.dto.resp;

import com.photoboothmap.backend.booth.utils.ReviewUtils;
import com.photoboothmap.backend.util.entity.TagType;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 개별 리뷰 정보를 담고 있는 DTO
public class RespReviewInfoDto {
    private float starRate;
    private String boothName;
    private Timestamp date;
    private Optional<List<String>> imageUrls;
    private List<TagType> userTags;
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public void setStarRate(Float starRate) {
        this.starRate = starRate;
    }

    public void setBoothName(String boothName) {
        this.boothName = boothName;
    }

    public void setReviewDate(Timestamp date) {
        this.date = date;
    }

    public void setImageUrls(Optional<List<String>> imageUrls) { this.imageUrls = imageUrls; }

    public void setUserTags(List<String> userTags){
        this.userTags = ReviewUtils.convertStringToTagEnum(userTags);
    }
}
