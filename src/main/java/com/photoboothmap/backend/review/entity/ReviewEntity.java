package com.photoboothmap.backend.review.entity;

import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.util.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="review")
@DynamicInsert // insert/update 시 null 아닌 값들만 다루기
@DynamicUpdate // default값 적용에 필요
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReviewEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photoBooth")
    private BoothEntity photoBooth;

    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @Column
    private String content;

    @Column(nullable = false)
    private Float starRate;

    @ColumnDefault("'active'")
    private String status;

    @OneToMany(mappedBy = "review")
    private List<ImageEntity> imageUrls;

    @OneToMany(mappedBy = "review")
    private List<TagEntity> tags;

}
