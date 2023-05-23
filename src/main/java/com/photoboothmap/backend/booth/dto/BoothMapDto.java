package com.photoboothmap.backend.booth.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoothMapDto {

    private Long boothIdx;

    private String brand;

    private Double latitude;

    private Double longitude;
}