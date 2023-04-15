package com.photoboothmap.backend.booth.entity;

import com.photoboothmap.backend.brand.entity.BrandEntity;
import com.photoboothmap.backend.util.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="photo_booth")
@DynamicInsert // insert/update 시 null 아닌 값들만 다루기
@DynamicUpdate // default값 적용에 필요
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BoothEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boothIdx;

    @ManyToOne
    @JoinColumn(name = "brandIdx")
    private BrandEntity brandIdx;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String newAddress;

    @Column(nullable = false)
    private float xCoordinate;

    @Column(nullable = false)
    private float yCoordinate;

    @Column(columnDefinition = "varchar(10) default 'active'")
    private String status;

}


