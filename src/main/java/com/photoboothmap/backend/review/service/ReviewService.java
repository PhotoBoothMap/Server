package com.photoboothmap.backend.review.service;

import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.booth.service.BoothService;
import com.photoboothmap.backend.review.entity.ReviewEntity;
import com.photoboothmap.backend.review.repository.ReviewRepository;
import com.photoboothmap.backend.review.utils.ReviewUtils;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final BoothService boothService;

    public Map<String, Object> getReviewByBooth(Long id, int count) throws BaseException {
        try {
            BoothEntity entityById = boothService.getEntityById(id);

            Pageable paging = PageRequest.of(count/10, 10);
            List<ReviewEntity> reviewEntities = reviewRepository.findByPhotoBoothOrderByCreatedAtDesc(entityById, paging);

            Map<String, Object> reviewList = new HashMap<>() {{
                put("review", ReviewUtils.toReviewListDto(reviewEntities));
            }};
            return reviewList;

        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(ResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
