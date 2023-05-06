package com.photoboothmap.backend.booth.service;

import com.photoboothmap.backend.booth.dto.BoothMapDto;
import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.booth.repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;

    public Map<String, Object> getBoothMap(Double curx, Double cury, Double nex, Double ney) {
        List<BoothEntity> entityList = boothRepository.findBoothMap(curx, cury, nex-curx, ney-cury);

        List<BoothMapDto> list = entityList.stream()
                .map(b -> BoothMapDto.builder()
                        .boothIdx(b.getBoothIdx())
                        .brand(b.getBrandIdx().getName())
                        .latitude(b.getLatitude())
                        .longitude(b.getLongitude())
                        .build())
                .collect(Collectors.toList());

        Map<String, Object> boothMap = new HashMap<>() {{
            put("boothList", list);
        }};

        return boothMap;
    }

}
