package com.photoboothmap.backend.booth.service;

import com.photoboothmap.backend.booth.dto.reviewDto.ReqCreateReviewDto;
import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.booth.repository.BoothRepository;
import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import com.photoboothmap.backend.review.entity.ReviewEntity;
import com.photoboothmap.backend.review.entity.TagEntity;
import com.photoboothmap.backend.review.repository.ReviewRepository;
import com.photoboothmap.backend.review.repository.TagRepository;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothDetailService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final BoothRepository boothRepository;
    private final TagRepository tagRepository;

    public void postBoothReview(Long userId, Long boothId, ReqCreateReviewDto reqCreateReviewDto) throws BaseException {
        try {
            Member member = memberRepository.getById(userId);
            BoothEntity booth = boothRepository.getById(boothId);

            ReviewEntity newReview = ReviewEntity.builder()
                    .photoBooth(booth)
                    .member(member)
                    .content(reqCreateReviewDto.getContent().orElse(null))
                    .starRate(reqCreateReviewDto.getStarRate())
                    .build();

            reviewRepository.save(newReview);

            List<TagEntity> tagEntityList = reqCreateReviewDto.getUserTags().stream()
                    .map(tag -> TagEntity.builder()
                            .review(newReview)
                            .tag(tag)
                            .build()
                    ).collect(Collectors.toList());

            tagRepository.saveAll(tagEntityList);

        } catch (EntityNotFoundException e){
            throw new BaseException(ResponseStatus.BAD_REQUEST);
        }
    }
}
