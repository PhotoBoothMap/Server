package com.photoboothmap.backend.booth.controller;

import com.photoboothmap.backend.booth.dto.reviewDto.ReqCreateReviewDto;
import com.photoboothmap.backend.booth.dto.reviewDto.SaveImageRes;
import com.photoboothmap.backend.booth.service.BoothDetailService;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class BoothDetailController {
    private final BoothDetailService boothDetailService;

    @ResponseBody
    @PostMapping("/booth/{boothId}/review")
    public ResponseEntity<BaseResponse> postBoothReview(
            @PathVariable Long boothId,
            @RequestBody ReqCreateReviewDto reqCreateReviewDto
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        try{
            this.boothDetailService.postBoothReview(userEmail, boothId, reqCreateReviewDto);
            return new BaseResponse<>("리뷰를 등록하였습니다.").convert();
        } catch (BaseException ex){
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

    @ResponseBody
    @PostMapping("/booth/{boothId}/image")
    public ResponseEntity<BaseResponse> postImage(
            @PathVariable Long boothId,
            @RequestParam(value = "file") MultipartFile file
    ){
        try{
            SaveImageRes imageRes = this.boothDetailService.saveImage(boothId, file);
            return new BaseResponse<>(imageRes).convert();
        } catch (BaseException ex){
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

    @ResponseBody
    @DeleteMapping("/image")
    public ResponseEntity<BaseResponse> deleteTempImage(
            @RequestParam String imageUrl
    ){
        boothDetailService.deleteTempImage(imageUrl);
        return new BaseResponse<>(true).convert();
    }
}
