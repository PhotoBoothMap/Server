package com.photoboothmap.backend.review.entity;

import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.user.entity.UserEntity;
import com.photoboothmap.backend.util.entity.BaseTimeEntity;
import com.photoboothmap.backend.util.entity.LoginType;
import com.photoboothmap.backend.util.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

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
    private Long brandIdx;

    @ManyToOne
    @JoinColumn(name = "boothIdx")
    private BoothEntity boothIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private UserEntity userIdx;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Float starRate;

    @ColumnDefault("'active'")
    private String status;

}
