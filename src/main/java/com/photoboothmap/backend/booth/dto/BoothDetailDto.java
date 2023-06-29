package com.photoboothmap.backend.booth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoothDetailDto {
    private Long id;

    private String brand;

    private String name;

    private String address;

    private Float score;

    private Long reviewNum;
}