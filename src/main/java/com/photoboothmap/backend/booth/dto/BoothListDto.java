package com.photoboothmap.backend.booth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoothListDto {
    private Long boothIdx;

    private String brand;

    private String name;

    private String address;

    private int distance;

    private Float score;

    private Long reviewNum;

    private Double latitude;

    private Double longitude;

}
