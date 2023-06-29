package com.photoboothmap.backend.review.repository;

import com.photoboothmap.backend.review.entity.ReviewEntity;
import com.photoboothmap.backend.review.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    List<TagEntity> findByReview(ReviewEntity review);

    @Query(value = "select tag, count(tag) as cnt " +
            "from review_tag " +
            "where review in (" +
                "select id from review " +
                "where photo_booth = :boothIdx)" +
            "group by tag " +
            "order by cnt desc " +
            "limit 3"
    , nativeQuery = true)
    List<Tuple> countTop3TagsByBooth_Id(@Param("boothIdx") Long boothIdx);
}
