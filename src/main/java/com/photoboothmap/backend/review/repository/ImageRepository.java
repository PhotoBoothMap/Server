package com.photoboothmap.backend.review.repository;

import com.photoboothmap.backend.review.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
