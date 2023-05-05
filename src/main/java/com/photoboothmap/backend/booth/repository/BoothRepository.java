package com.photoboothmap.backend.booth.repository;

import com.photoboothmap.backend.booth.entity.BoothEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<BoothEntity, Long> {
}
