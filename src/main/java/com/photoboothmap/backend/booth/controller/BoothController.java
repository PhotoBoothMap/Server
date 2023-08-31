package com.photoboothmap.backend.booth.controller;

import com.photoboothmap.backend.booth.service.BoothService;
import com.photoboothmap.backend.review.service.ReviewService;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;
    private final ReviewService reviewService;

    @ResponseBody
    @GetMapping("/map")
    public ResponseEntity<BaseResponse> getBoothMap(
            @RequestParam Double clng,
            @RequestParam Double clat,
            @RequestParam Double nlng,
            @RequestParam Double nlat,
            @RequestParam String filter) {
        try {
            Map<String, Object> boothList = boothService.getBoothMap(clng, clat, nlng, nlat, filter);
            return new BaseResponse<>(boothList).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

    @ResponseBody
    @GetMapping("/map/list")
    public ResponseEntity<BaseResponse> getBoothList(
            @RequestParam Double clng,
            @RequestParam Double clat,
            @RequestParam int count,
            @RequestParam String filter) {
        try {
            Map<String, Object> boothList = boothService.getBoothList(clng, clat, count, filter);
            return new BaseResponse<>(boothList).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

    @ResponseBody
    @GetMapping("/map/search")
    public ResponseEntity<BaseResponse> getBoothSearch(
            @RequestParam Double clng,
            @RequestParam Double clat,
            @RequestParam Double nlng,
            @RequestParam Double nlat,
            @RequestParam String keyword) {
        try {
            Map<String, Object> boothList = boothService.getBoothSearch(clng, clat, nlng, nlat, keyword);
            return new BaseResponse<>(boothList).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

    @ResponseBody
    @GetMapping("/booth/{id}/review")
    public ResponseEntity<BaseResponse> getReviewByBooth(
            @PathVariable Long id,
            @RequestParam int count) {
        try {
            Map<String, Object> reviewList = reviewService.getReviewByBooth(id, count);
            return new BaseResponse<>(reviewList).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }


    @ResponseBody
    @GetMapping("/booth/{id}")
    public ResponseEntity<BaseResponse> getBoothDetail(
            @PathVariable Long id) {
        try {
            Map<String, Object> boothDetail = boothService.getBoothDetail(id);
            return new BaseResponse<>(boothDetail).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }

    }
}
