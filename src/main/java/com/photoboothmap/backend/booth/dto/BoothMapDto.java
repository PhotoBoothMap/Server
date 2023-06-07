package com.photoboothmap.backend.booth.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoothMapDto {

    private Long id;

    private String brand;

    private CoordinateDto coordinate;
}