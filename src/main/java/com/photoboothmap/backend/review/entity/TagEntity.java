package com.photoboothmap.backend.review.entity;

import com.photoboothmap.backend.util.entity.BaseTimeEntity;
import com.photoboothmap.backend.util.entity.TagType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="review_tag")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TagEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review")
    private ReviewEntity review;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TagType tag;

    @ColumnDefault("'active'")
    private String status;

}