package com.photoboothmap.backend.booth.service;

import com.photoboothmap.backend.booth.dto.BoothListDto;
import com.photoboothmap.backend.booth.dto.BoothMapDto;
import com.photoboothmap.backend.booth.dto.CoordinateDto;
import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.booth.repository.BoothRepository;
import com.photoboothmap.backend.brand.repository.BrandRepository;
import com.photoboothmap.backend.review.repository.ReviewRepository;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final String basicBrand = "포토이즘,하루필름,포토매틱,인생네컷,셀픽스,포토그레이";

    public Map<String, Object> getBoothMap(Double curx, Double cury, Double nex, Double ney, String filter) throws BaseException {
        try {
            List<BoothEntity> boothList = new ArrayList<>();

            if (!filter.isBlank()) {
                Boolean include = checkFilter(filter);
                List<Long> filterNum = getBrandList(filter, include);

                boothList = boothRepository.findBoothMap(curx, cury, nex-curx, ney-cury, filterNum, include);
            }

            List<BoothMapDto> list = convertToBoothMapDto(boothList);

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
            List<Tuple> boothList = new ArrayList<>();

            if (!filter.isBlank()) {
                Boolean include = checkFilter(filter);
                List<Long> filterNum = getBrandList(filter, include);

                boothList = boothRepository.findBoothList(curx, cury, count, filterNum, include);
            }

            List<BoothListDto> list = boothList.stream()
                    .map(b -> BoothListDto.builder()
                            .id(b.get("id", BigInteger.class).longValue())
                            .brand(brandRepository.findById(b.get("brand", BigInteger.class).longValue()).get().getName())
                            .name(b.get("name", String.class))
                            .address(b.get("address", String.class))
                            .distance((int) Math.round(b.get("distance", Double.class)))
                            .score(reviewRepository.averageStarRateByBoothIdx(b.get("id", BigInteger.class).longValue()))
                            .reviewNum(reviewRepository.countByPhotoBooth_Id(b.get("id", BigInteger.class).longValue()))
                            .coordinate(CoordinateDto.builder()
                                    .lat(b.get("latitude", Double.class))
                                    .lng(b.get("longitude", Double.class))
                                    .build())
                            .build()
                    ).collect(Collectors.toList());

            Map<String, Object> boothMap = new HashMap<>() {{
                put("boothList", list);
            }};
            return boothMap;

        } catch (NullPointerException e) {
            throw new BaseException(ResponseStatus.WRONG_BRAND_NAME);
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(ResponseStatus.WRONG_LATLNG_RANGE);
        }
    }

    public Map<String, Object> getBoothSearch(Double curx, Double cury, Double nex, Double ney, String keyword) throws BaseException {
        try {
            if (keyword.isBlank()) {
                throw new BaseException(ResponseStatus.EMPTY_KEYWORD);
            }

            List<BoothEntity> boothList = boothRepository.findBoothSearch(
                    curx, cury, nex-curx, ney-cury, brandRepository.getBrandEntityByName(keyword).getId());

            List<BoothMapDto> list = convertToBoothMapDto(boothList);

            Map<String, Object> boothMap = new HashMap<>() {{
                put("boothList", list);
            }};
            return boothMap;

        } catch (NullPointerException e) {
            throw new BaseException(ResponseStatus.WRONG_BRAND_NAME);
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        }
    }

    public List<BoothMapDto> convertToBoothMapDto(List<BoothEntity> boothList) {
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

    public Boolean checkFilter(String filter) {
        if (filter.contains("기타")) {
            return false;
        } else {
            return true;
        }
    }

    public List<Long> getBrandList(String filter, Boolean include) throws NullPointerException {
        List<String> filterList = List.of(filter.split(","));

        if (include.equals(false)) {
            // 제외하는 방향으로
            List<Long> filterNum = getBrandIds(List.of(basicBrand.split(",")));
            filterNum.removeAll(getBrandIds(filterList));
            return filterNum;
        } else {
            // 포함하는 방향으로
            return getBrandIds(filterList);
        }
    }

    public List<Long> getBrandIds(List<String> brandList) throws NullPointerException {
        return brandList.stream()
                .map(b -> brandRepository.getBrandEntityByName(b).getId())
                .collect(Collectors.toList());
    }
}
