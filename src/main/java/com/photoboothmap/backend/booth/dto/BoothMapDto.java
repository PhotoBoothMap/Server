package com.photoboothmap.backend.booth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoothMapDto {

    private Long boothIdx;

    private String brand;

    private Double latitude;

    private Double longitude;
}