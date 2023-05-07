package com.photoboothmap.backend.brand.entity;

import com.photoboothmap.backend.util.entity.BaseTimeEntity;
import com.photoboothmap.backend.util.entity.FrameShape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="frame")
@DynamicInsert // insert/update 시 null 아닌 값들만 다루기
@DynamicUpdate // default값 적용에 필요
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FrameEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brandIdx")
    private BrandEntity brand;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FrameShape shape;

    @Column(nullable = false)
    private int price;

    @ColumnDefault("'active'")
    private String status;
}
