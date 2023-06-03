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
    @Query(value = "select * from photo_booth b where " +
            "b.longitude between (:lng-:width) and (:lng+:width) and " +
            "b.latitude between (:lat-:height) and (:lat+:height) and " +
            "brand in :includeList"
            , nativeQuery = true)
    List<BoothEntity> findBoothMapIn(
            @Param("lng") Double curx,
            @Param("lat") Double cury,
            @Param("width") Double width,
            @Param("height") Double height,
            @Param("includeList") List<Long> includeList);

    @Query(value = "select * from photo_booth b where " +
            "b.longitude between (:lng-:width) and (:lng+:width) and " +
            "b.latitude between (:lat-:height) and (:lat+:height) and " +
            "(COALESCE(:excludeList, 0) = 0 or brand not in :excludeList)"
            , nativeQuery = true)
    List<BoothEntity> findBoothMapNotIn(
            @Param("lng") Double curx,
            @Param("lat") Double cury,
            @Param("width") Double width,
            @Param("height") Double height,
            @Param("excludeList") List<Long> excludeList);

    @Query(value = "select *, ST_Distance_Sphere(point(:lng, :lat), point(longitude, latitude)) as distance " +
            "from photo_booth b " +
            "where brand in :includeList " +
            "order by distance " +
            "limit :offset, 10"
            , nativeQuery = true)
    List<Tuple> findBoothListIn(
            @Param("lng") Double curx,
            @Param("lat") Double cury,
            @Param("offset") int offset,
            @Param("includeList") List<Long> includeList);

    @Query(value = "select *, ST_Distance_Sphere(point(:lng, :lat), point(longitude, latitude)) as distance " +
            "from photo_booth b " +
            "where COALESCE(:excludeList, 0) = 0 or brand not in :excludeList "+
            "order by distance " +
            "limit :offset, 10"
            , nativeQuery = true)
    List<Tuple> findBoothListNotIn(
            @Param("lng") Double curx,
            @Param("lat") Double cury,
            @Param("offset") int offset,
            @Param("excludeList") List<Long> excludeList);
}
