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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;
    private final BrandRepository brandRepository;
    private final ReviewRepository reviewRepository;

    public Map<String, Object> getBoothMap(Double curx, Double cury, Double nex, Double ney, String filter) throws BaseException {
        try {
            List<String> filterList = List.of(filter.split(","));

            List<BoothEntity> boothList = null;
            if (filterList.contains("기타")) {
                // 제외하는 방향으로
                List<String> rep = new ArrayList<>(Arrays.asList("포토이즘", "하루필름", "포토시그니처", "인생네컷", "셀픽스"));
                rep.removeAll(filterList);
                boothList = boothRepository.findBoothMapNotIn(curx, cury, nex-curx, ney-cury, getBrandIds(rep));
            } else {
                // 포함하는 방향으로
                boothList = boothRepository.findBoothMapIn(curx, cury, nex-curx, ney-cury, getBrandIds(filterList));
            }

            List<BoothMapDto> list = boothList.stream()
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

        } catch (NullPointerException e) {
            throw new BaseException(ResponseStatus.WRONG_BRAND_NAME);
        }
    }

    public Map<String, Object> getBoothList(Double curx, Double cury, int count, String filter) throws BaseException {
        try {
            List<String> filterList = List.of(filter.split(","));

            List<Tuple> boothList = null;
            if (filterList.contains("기타")) {
                // 제외하는 방향으로
                List<String> rep = new ArrayList<>(Arrays.asList("포토이즘", "하루필름", "포토시그니처", "인생네컷", "셀픽스"));
                rep.removeAll(filterList);
                boothList = boothRepository.findBoothListNotIn(curx, cury, count, getBrandIds(rep));
            } else {
                // 포함하는 방향으로
                boothList = boothRepository.findBoothListIn(curx, cury, count, getBrandIds(filterList));
            }

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

        } catch (NullPointerException e) {
            throw new BaseException(ResponseStatus.WRONG_BRAND_NAME);
        } catch (Exception e) {
            throw new BaseException(ResponseStatus.WRONG_LATLNG_RANGE);
        }
    }

    public List<Long> getBrandIds(List<String> brandList) throws NullPointerException {
        return brandList.stream()
                .map(b -> brandRepository.getBrandEntityByName(b).getId())
                .collect(Collectors.toList());
    }
}
