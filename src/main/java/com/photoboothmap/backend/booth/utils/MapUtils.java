package com.photoboothmap.backend.booth.utils;

import com.photoboothmap.backend.booth.dto.BoothMapDto;
import com.photoboothmap.backend.booth.dto.CoordinateDto;
import com.photoboothmap.backend.booth.entity.BoothEntity;

import java.util.List;
import java.util.stream.Collectors;

public class MapUtils {
    public static List<BoothMapDto> convertToBoothMapDto(List<BoothEntity> boothList) {
        return boothList.stream()
                .map(b -> BoothMapDto.builder()
                        .id(b.getId())
                        .brand(b.getBrand().getName())
                        .coordinate(CoordinateDto.builder()
                                .lat(b.getLatitude())
                                .lng(b.getLongitude())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    public static Boolean checkFilter(String filter) {
        if (filter.contains("기타")) {
            return false;
        } else {
            return true;
        }
    }
}
