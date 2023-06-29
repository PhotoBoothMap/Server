package com.photoboothmap.backend.booth.repository;

import com.photoboothmap.backend.booth.entity.BoothEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoothRepository extends JpaRepository<BoothEntity, Long> {
    @Query(value = "select * from photo_booth b where " +
            "b.longitude between (:lng-:width) and (:lng+:width) and " +
            "b.latitude between (:lat-:height) and (:lat+:height) and " +
            "IF(:include, brand IN (:list)," +
            "(COALESCE(:list, 0) = 0 OR brand NOT IN (:list)))"
            , nativeQuery = true)
    List<BoothEntity> findBoothMap(
            @Param("lng") Double clng,
            @Param("lat") Double clat,
            @Param("width") Double width,
            @Param("height") Double height,
            @Param("list") List<Long> list,
            @Param("include") Boolean include);

    @Query(value = "select *, ST_Distance_Sphere(point(:lng, :lat), point(longitude, latitude)) as distance " +
            "from photo_booth b " +
            "where IF(:include, brand IN (:list)," +
            "(COALESCE(:list, 0) = 0 OR brand NOT IN (:list))) " +
            "order by distance " +
            "limit :offset, 10"
            , nativeQuery = true)
    List<Tuple> findBoothList(
            @Param("lng") Double clng,
            @Param("lat") Double clat,
            @Param("offset") int offset,
            @Param("list") List<Long> list,
            @Param("include") Boolean include);

    @Query(value = "select * " +
            "from photo_booth b " +
            "where b.longitude between (:lng-:width) and (:lng+:width) and " +
            "b.latitude between (:lat-:height) and (:lat+:height) and " +
            "b.brand = :brandNum"
            , nativeQuery = true)
    List<BoothEntity> findBoothSearch(
            @Param("lng") Double clng,
            @Param("lat") Double clat,
            @Param("width") Double width,
            @Param("height") Double height,
            @Param("brandNum") Long brandNum);

    Optional<BoothEntity> findByIdAndStatus(Long id, String status);
  
    BoothEntity getById(Long id);
}
