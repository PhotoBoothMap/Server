package com.photoboothmap.backend.booth.service;

import com.photoboothmap.backend.booth.dto.BoothListDto;
import com.photoboothmap.backend.booth.dto.BoothMapDto;
import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.booth.repository.BoothRepository;
import com.photoboothmap.backend.brand.repository.BrandRepository;
import com.photoboothmap.backend.review.repository.ReviewRepository;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;
    private final BrandRepository brandRepository;
    private final ReviewRepository reviewRepository;

    public Map<String, Object> getBoothMap(Double curx, Double cury, Double nex, Double ney, String filter) {
        List<BoothEntity> entityList = boothRepository.findBoothMap(curx, cury, nex-curx, ney-cury);

        List<String> filterList = List.of(filter.split(","));

        Predicate<BoothEntity> filterMethod = null;
        if (filterList.contains("기타")) {
            // 제외하는 방향으로
            List<String> rep = new ArrayList<>(Arrays.asList("포토이즘", "하루필름", "포토시그니처", "인생네컷", "셀픽스"));
            rep.removeAll(filterList);
            filterMethod = b -> !rep.contains(b.getBrand().getName());
        } else {
            // 포함하는 방향으로
            filterMethod = b -> filterList.contains(b.getBrand().getName());
        }

        List<BoothMapDto> list = entityList.stream()
                .filter(filterMethod)
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

    public Map<String, Object> getBoothList(Double curx, Double cury, int count, String filter) throws BaseException {
        try {
            List<Tuple> boothList = boothRepository.findBoothList(curx, cury, count);

            List<String> filterList = List.of(filter.split(","));

            Predicate<Tuple> filterMethod = null;
            if (filterList.contains("기타")) {
                // 제외하는 방향으로
                List<String> rep = new ArrayList<>(Arrays.asList("포토이즘", "하루필름", "포토시그니처", "인생네컷", "셀픽스"));
                rep.removeAll(filterList);
                filterMethod = b -> !rep.contains(brandRepository.findById(b.get("brand", BigInteger.class).longValue()).get().getName());
            } else {
                // 포함하는 방향으로
                filterMethod = b -> filterList.contains(brandRepository.findById(b.get("brand", BigInteger.class).longValue()).get().getName());
            }

            List<BoothListDto> list = boothList.stream()
                    .filter(filterMethod)
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

        } catch (Exception e) {
            throw new BaseException(ResponseStatus.WRONG_LATLNG_RANGE);
        }
    }
}
