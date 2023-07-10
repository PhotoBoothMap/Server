package com.photoboothmap.backend.booth.service;

import com.photoboothmap.backend.booth.dto.BoothDetailDto;
import com.photoboothmap.backend.booth.dto.BoothListDto;
import com.photoboothmap.backend.booth.dto.BoothMapDto;
import com.photoboothmap.backend.booth.dto.CoordinateDto;
import com.photoboothmap.backend.booth.entity.BoothEntity;
import com.photoboothmap.backend.booth.repository.BoothRepository;
import com.photoboothmap.backend.booth.utils.MapUtils;
import com.photoboothmap.backend.brand.repository.BrandRepository;
import com.photoboothmap.backend.review.dto.ReviewListDto;
import com.photoboothmap.backend.review.entity.ReviewEntity;
import com.photoboothmap.backend.review.repository.ReviewRepository;
import com.photoboothmap.backend.review.repository.TagRepository;
import com.photoboothmap.backend.review.utils.ReviewUtils;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.ResponseStatus;
import com.photoboothmap.backend.util.entity.TagType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoothService {

    private final BoothRepository boothRepository;
    private final BrandRepository brandRepository;
    private final ReviewRepository reviewRepository;
    private final TagRepository tagRepository;
    private final String basicBrand = "포토이즘,하루필름,인생네컷,셀픽스,포토시그니처,포토그레이,모노맨션";

    public Map<String, Object> getBoothMap(Double clng, Double clat, Double nlng, Double nlat, String filter) throws BaseException {
        try {
            List<BoothEntity> boothList = new ArrayList<>();

            if (!filter.isBlank()) {
                Boolean include = MapUtils.checkFilter(filter);
                List<Long> filterNum = getBrandList(filter, include);

                boothList = boothRepository.findBoothMap(clng, clat, nlng-clng, nlat-clat, filterNum, include);
            }

            List<BoothMapDto> list = MapUtils.convertToBoothMapDto(boothList);

            Map<String, Object> boothMap = new HashMap<>() {{
                put("boothList", list);
            }};
            log.trace("{} : filter {}, ({} {}), ({} {})", this.getClass(), filter, clng, clat, nlng, nlat);
            return boothMap;

        } catch (NullPointerException e) {
            log.warn("{} : {}", this.getClass().getName(), "wrong brand name");
            throw new BaseException(ResponseStatus.WRONG_BRAND_NAME);
        }
    }

    public Map<String, Object> getBoothList(Double clng, Double clat, int count, String filter) throws BaseException {
        try {
            List<Tuple> boothList = new ArrayList<>();

            if (!filter.isBlank()) {
                Boolean include = MapUtils.checkFilter(filter);
                List<Long> filterNum = getBrandList(filter, include);

                boothList = boothRepository.findBoothList(clng, clat, count, filterNum, include);
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
            log.trace("{} : filter {}, count {}, ({} {})", this.getClass(), filter, count, clng, clat);
            return boothMap;

        } catch (NullPointerException e) {
            log.warn("{} : {}", this.getClass().getName(), "wrong brand name");
            throw new BaseException(ResponseStatus.WRONG_BRAND_NAME);
        } catch (DataIntegrityViolationException e) {
            log.warn("{} : {}", this.getClass().getName(), "wrong lat/lng range");
            throw new BaseException(ResponseStatus.WRONG_LATLNG_RANGE);
        }
    }

    public Map<String, Object> getBoothSearch(Double clng, Double clat, Double nlng, Double nlat, String keyword) throws BaseException {
        try {
            if (keyword.isBlank()) {
                throw new BaseException(ResponseStatus.EMPTY_KEYWORD);
            }

            List<BoothEntity> boothList = boothRepository.findBoothSearch(
                    clng, clat, nlng-clng, nlat-clat, brandRepository.getBrandEntityByName(keyword).getId());

            List<BoothMapDto> list = MapUtils.convertToBoothMapDto(boothList);

            Map<String, Object> boothMap = new HashMap<>() {{
                put("boothList", list);
            }};
            log.info("{} : keyword {}, ({} {}), ({} {})", this.getClass(), keyword, clng, clat, nlng, nlat);
            return boothMap;

        } catch (NullPointerException e) {
            log.warn("{} : {}", this.getClass().getName(), "wrong brand name");
            throw new BaseException(ResponseStatus.WRONG_BRAND_NAME);
        } catch (BaseException e) {
            log.warn("{} : {}", this.getClass().getName(), e.getStatus().name());
            throw new BaseException(e.getStatus());
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

    public BoothEntity getEntityById(Long id) throws BaseException {
        Optional<BoothEntity> boothOptional = boothRepository.findByIdAndStatus(id, "active");
        if (boothOptional.isEmpty()) {
            throw new BaseException(ResponseStatus.INVALID_BOOTH);
        } else {
            return boothOptional.get();
        }
    }

    public Map<String, Object> getBoothDetail(Long id) throws BaseException {
        try {
            BoothEntity booth = getEntityById(id);
            BoothDetailDto boothDetail = BoothDetailDto.builder()
                    .id(booth.getId())
                    .brand(booth.getBrand().getName())
                    .name(booth.getName())
                    .address(booth.getAddress())
                    .score(reviewRepository.averageStarRateByBoothIdx(booth.getId()))
                    .reviewNum(reviewRepository.countByPhotoBooth_Id(booth.getId()))
                    .tagNum(tagRepository.countByReview_PhotoBooth_Id(booth.getId()))
                    .build();


            List<Tuple> tags = tagRepository.countTop3TagsByBooth_Id(id);

            Map<String, Long> tagCount = new LinkedHashMap<>();
            tags.stream().
                    forEach(tag -> tagCount.put(
                            TagType.valueOf(tag.get("tag", String.class)).getTag(),
                            tag.get("cnt", BigInteger.class).longValue())
                    );


            Pageable paging = PageRequest.of(0, 3);
            List<ReviewEntity> reviewEntities = reviewRepository.findByPhotoBoothOrderByCreatedAtDesc(booth, paging);
            List<ReviewListDto> reviewList = ReviewUtils.toReviewListDto(reviewEntities);


            Map<String, Object> res = new HashMap<>() {{
                put("boothDetail", boothDetail);
                put("userTags", tagCount);
                put("review", reviewList);
            }};
            log.info("{} : booth id {}", this.getClass(), id);
            return res;

        } catch (BaseException e) {
            log.warn("{} : {}", this.getClass().getName(), e.getStatus().name());
            throw new BaseException(e.getStatus());
        }
    }
}
