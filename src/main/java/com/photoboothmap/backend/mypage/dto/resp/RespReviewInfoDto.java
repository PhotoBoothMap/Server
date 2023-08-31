package com.photoboothmap.backend.mypage.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private float score;
    private String brand;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Timestamp date;
    private Optional<List<byte[]>> imgFile;
    private List<String> userTags;
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public void setStarRate(Float starRate) {
        this.score = starRate;
    }

    public void setName(String boothName) {
        this.name = boothName;
    }

    public void setBrand(String brand) {this.brand = brand;}

    public void setReviewDate(Timestamp date) {
        this.date = date;
    }

    public void setImgFile(Optional<List<byte[]>> imgFile) {this.imgFile = imgFile;}

    public void setUserTags(List<String> userTags){
        this.userTags = userTags;
    }
}
