package com.photoboothmap.backend.review.repository;

import com.photoboothmap.backend.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Long countByBoothIdx_BoothIdx(Long boothIdx);

    @Query(value = "select avg(star_rate) from whereisphoto.review where booth_idx = :boothIdx"
            , nativeQuery = true)
    Float averageStarRateByBoothIdx(Long boothIdx);
}
