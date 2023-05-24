package com.photoboothmap.backend.review.repository;

import com.photoboothmap.backend.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Long countByPhotoBooth_Id(Long boothIdx);

    @Query(value = "select avg(star_rate) from review where photo_booth = :boothIdx"
            , nativeQuery = true)
    Float averageStarRateByBoothIdx(@Param("boothIdx") Long boothIdx);
}
