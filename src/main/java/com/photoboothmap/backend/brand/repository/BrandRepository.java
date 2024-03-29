package com.photoboothmap.backend.brand.repository;

import com.photoboothmap.backend.brand.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    BrandEntity getBrandEntityByName(String name);
}
