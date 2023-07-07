package com.photoboothmap.backend.booth.service;

import com.photoboothmap.backend.booth.dto.reviewDto.ReqCreateReviewDto;
import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.booth.repository.BoothRepository;
import com.photoboothmap.backend.booth.utils.ReviewUtils;
import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import com.photoboothmap.backend.review.entity.ImageEntity;
import com.photoboothmap.backend.review.entity.ReviewEntity;
import com.photoboothmap.backend.review.entity.TagEntity;
import com.photoboothmap.backend.review.repository.ImageRepository;
import com.photoboothmap.backend.review.repository.ReviewRepository;
import com.photoboothmap.backend.review.repository.TagRepository;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoothDetailService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final BoothRepository boothRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;

    @Transactional(rollbackFor = BaseException.class)
    public void postBoothReview(
            String userEmail,
            Long boothId,
            ReqCreateReviewDto reqCreateReviewDto
    ) throws BaseException {
        try {
            Member member = memberRepository.getByEmail(userEmail);
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

            /**
             * reqCreateReviewDto.getImageUrls()이 not null 인 경우에만 하고 싶은데 좋은 방법을 모르겠어요!
             * 리뷰 남겨주시면 감사드리겠습니다
             */


            if (!reqCreateReviewDto.getImageUrls().isEmpty()){
                List<String> imageUrlList = reqCreateReviewDto.getImageUrls().get();
                List<ImageEntity> imageEntityList = imageUrlList.stream()
                        .map(url -> ImageEntity.builder()
                                .review(newReview)
                                .imgUrl(url.toString())
                                .build()
                        ).collect(Collectors.toList());

                imageRepository.saveAll(imageEntityList);
            }


        } catch (EntityNotFoundException e){
            throw new BaseException(ResponseStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("{}: Exception {}", this.getClass().getName(), e);
            throw new BaseException(ResponseStatus.SERVICE_UNAVAILABLE);
        }
    }

    public String saveImage(Long boothId, MultipartFile file) throws BaseException {
        try{
            Path currentPath = Paths.get("");
            String path = currentPath.toAbsolutePath().toString();
            log.info("current path {}", path);

            String fileExtension = ReviewUtils.getFileExtension(file.getOriginalFilename());
            String targetDirectoryPath = path + "/image/" + "booth-" + boothId;
            File targetDirectory = new File(targetDirectoryPath);
            if(!targetDirectory.exists()){
                targetDirectory.mkdirs();
            }
            Path targetDirectoryPathObj = Paths.get(targetDirectoryPath);

            String imageFileName = "image-" + UUID.randomUUID() + fileExtension;
            Files.copy(file.getInputStream(), targetDirectoryPathObj.resolve(imageFileName));
            log.info("save image {}", imageFileName);
            return targetDirectoryPath + File.separator + imageFileName;

        } catch (IOException e){
            log.error("{}: IO Exception {}", this.getClass().getName(), e);
            throw new BaseException(ResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("{}: Exception {}", this.getClass().getName(), e);
            throw new BaseException(ResponseStatus.SERVICE_UNAVAILABLE);
        }
    }

    public void deleteTempImage(String imageUrl){
        File fileToDelete = new File(imageUrl);
        boolean result = fileToDelete.delete();
        log.info("delete image {} {}", imageUrl, result);
    }
}
