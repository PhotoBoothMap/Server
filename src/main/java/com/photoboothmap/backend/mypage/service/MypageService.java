package com.photoboothmap.backend.mypage.service;

import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import com.photoboothmap.backend.mypage.dto.resp.RespReviewInfoDto;
import com.photoboothmap.backend.mypage.dto.resp.RespReviewListDto;
import com.photoboothmap.backend.review.entity.ImageEntity;
import com.photoboothmap.backend.review.entity.ReviewEntity;
import com.photoboothmap.backend.review.entity.TagEntity;
import com.photoboothmap.backend.review.repository.ImageRepository;
import com.photoboothmap.backend.review.repository.ReviewRepository;
import com.photoboothmap.backend.review.repository.TagRepository;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.ResponseStatus;
import com.photoboothmap.backend.util.entity.TagType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MypageService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;

    public RespReviewListDto getReview(Long id) throws BaseException {

        Member member = memberRepository.findById(id).orElseThrow(() -> new BaseException(ResponseStatus.NO_MEMBER));
        Long userId = member.getId();

        List<ReviewEntity> userReviews = reviewRepository.findByMemberId(userId);

        List<RespReviewInfoDto> reviewInfos = userReviews.stream()
                .map(review -> {
                    BoothEntity booth = review.getPhotoBooth();

                    List<ImageEntity> images = imageRepository.findByReviewWithJoin(review);
                    List<String> imageUrls = new ArrayList<>();

                    if (images != null && !images.isEmpty()) {
                        imageUrls = images.stream()
                                .map(ImageEntity::getImgUrl)
                                .collect(Collectors.toList());
                    }

                    RespReviewInfoDto reviewInfo = new RespReviewInfoDto();

                    List<String> tagStrings = tagRepository.findByReview(review).stream()
                            .map(entity -> TagType.valueOf(entity.getTag().name()).getTag())
                            .collect(Collectors.toList());

                    // updateDate 확인
                    Timestamp updatedDate = Timestamp.valueOf(review.getUpdatedAt());
                    if (updatedDate == null) {
                        reviewInfo.setReviewDate(Timestamp.valueOf(review.getCreatedAt()));
                    } else {
                        reviewInfo.setReviewDate(Timestamp.valueOf(review.getUpdatedAt()));
                    }

                    reviewInfo.setContent(review.getContent());
                    reviewInfo.setStarRate(review.getStarRate());
                    reviewInfo.setBrand(booth.getBrand().getName());
                    reviewInfo.setName(booth.getName());
                    reviewInfo.setImageUrls(Optional.of(imageUrls));
                    reviewInfo.setUserTags(tagStrings);

                    return reviewInfo;
                })
                .collect(Collectors.toList());

        RespReviewListDto response = new RespReviewListDto();
        response.setReviews(reviewInfos);

        return response;
    }

}