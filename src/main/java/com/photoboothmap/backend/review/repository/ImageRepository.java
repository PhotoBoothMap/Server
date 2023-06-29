package com.photoboothmap.backend.review.repository;

import com.photoboothmap.backend.review.entity.ImageEntity;
import com.photoboothmap.backend.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    @Query("SELECT img FROM ImageEntity img JOIN FETCH img.review WHERE img.review = :review")
    List<ImageEntity> findByReviewWithJoin(@Param("review") ReviewEntity review);
}
