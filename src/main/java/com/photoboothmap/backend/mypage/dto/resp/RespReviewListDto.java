package com.photoboothmap.backend.mypage.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 리뷰 목록을 담고 있는 DTO
public class RespReviewListDto {
    private List<RespReviewInfoDto> reviewList;

    public void setReviews(List<RespReviewInfoDto> reviewInfos) {
        this.reviewList = reviewInfos;
    }
}