package com.photoboothmap.backend.booth.service;

import com.photoboothmap.backend.booth.dto.BoothListDto;
import com.photoboothmap.backend.booth.dto.BoothMapDto;
import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.booth.repository.BoothRepository;
import com.photoboothmap.backend.brand.repository.BrandRepository;
import com.photoboothmap.backend.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;
    private final BrandRepository brandRepository;
    private final ReviewRepository reviewRepository;

    public Map<String, Object> getBoothMap(Double curx, Double cury, Double nex, Double ney) {
        List<BoothEntity> entityList = boothRepository.findBoothMap(curx, cury, nex-curx, ney-cury);

        List<BoothMapDto> list = entityList.stream()
                .map(b -> BoothMapDto.builder()
                        .boothIdx(b.getId())
                        .brand(b.getBrand().getName())
                        .latitude(b.getLatitude())
                        .longitude(b.getLongitude())
                        .build())
                .collect(Collectors.toList());

        Map<String, Object> boothMap = new HashMap<>() {{
            put("boothList", list);
        }};

        return boothMap;
    }

    public Map<String, Object> getBoothList(Double curx, Double cury, int count) {
        List<Tuple> boothList = boothRepository.findBoothList(curx, cury, count);

        List<BoothListDto> list = boothList.stream()
                .map(b -> BoothListDto.builder()
                        .boothIdx(b.get("id", BigInteger.class).longValue())
                        .brand(brandRepository.findById(b.get("brand", BigInteger.class).longValue()).get().getName())
                        .name(b.get("name", String.class))
                        .address(b.get("address", String.class))
                        .distance((int) Math.round(b.get("distance", Double.class)))
                        .score(reviewRepository.averageStarRateByBoothIdx(b.get("id", BigInteger.class).longValue()))
                        .reviewNum(reviewRepository.countByPhotoBooth_Id(b.get("id", BigInteger.class).longValue()))
                        .latitude(b.get("latitude", Double.class))
                        .longitude(b.get("longitude", Double.class))
                        .build()
                ).collect(Collectors.toList());

        Map<String, Object> boothMap = new HashMap<>() {{
            put("boothList", list);
        }};

        return boothMap;
    }
}
