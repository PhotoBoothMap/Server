package com.photoboothmap.backend.booth.repository;

import com.photoboothmap.backend.booth.entity.BoothEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface BoothRepository extends JpaRepository<BoothEntity, Long> {
    @Query(value = "select * from whereisphoto.photo_booth b where " +
            "b.longitude between (:lng-:width) and (:lng+:width) and " +
            "b.latitude between (:lat-:height) and (:lat+:height)"
            , nativeQuery = true)
    List<BoothEntity> findBoothMap(
            @Param("lng") Double curx,
            @Param("lat") Double cury,
            @Param("width") Double width,
            @Param("height") Double height);

    @Query(value = "select *, ST_Distance_Sphere(point(:lng, :lat), point(longitude, latitude)) as distance from whereisphoto.photo_booth b " +
            "order by distance " +
            "limit :offset, 10"
            , nativeQuery = true)
    List<Tuple> findBoothList(
            @Param("lng") Double curx,
            @Param("lat") Double cury,
            @Param("offset") int offset);
}
