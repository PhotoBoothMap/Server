package com.photoboothmap.backend.brand.entity;

import com.photoboothmap.backend.util.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="brand")
@DynamicInsert // insert/update 시 null 아닌 값들만 다루기
@DynamicUpdate // default값 적용에 필요
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BrandEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandIdx;

    @Column(nullable = false)
    private String name;

    @ColumnDefault("active")
    private String status;

}
