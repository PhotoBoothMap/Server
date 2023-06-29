package com.photoboothmap.backend.review.repository;

import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.review.entity.ReviewEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Long countByPhotoBooth_Id(Long boothIdx);

    @Query(value = "select avg(star_rate) from review where photo_booth = :boothIdx"
            , nativeQuery = true)
    Float averageStarRateByBoothIdx(@Param("boothIdx") Long boothIdx);

    List<ReviewEntity> findByMemberId(Long id);
  
    List<ReviewEntity> findByPhotoBoothOrderByCreatedAtDesc(BoothEntity booth, Pageable paging);

}