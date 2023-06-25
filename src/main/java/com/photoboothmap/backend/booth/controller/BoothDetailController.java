package com.photoboothmap.backend.booth.controller;

import com.photoboothmap.backend.booth.dto.reviewDto.ReqCreateReviewDto;
import com.photoboothmap.backend.booth.service.BoothDetailService;
import com.photoboothmap.backend.booth.utils.ReviewUtils;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.BaseResponse;
import com.photoboothmap.backend.util.entity.TagType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoothDetailController {
    private final BoothDetailService boothDetailService;

    @ResponseBody
    @PostMapping("/booth/{boothId}/review")
    public ResponseEntity<BaseResponse> postBoothReview(
            @PathVariable Long boothId,
//            @RequestParam("files") MultipartFile[] files,
            @RequestBody ReqCreateReviewDto reqCreateReviewDto
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        try{
            this.boothDetailService.postBoothReview(userId, boothId, reqCreateReviewDto);
            return new BaseResponse<>(reqCreateReviewDto.getUserTags()).convert();
        } catch (BaseException ex){
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }
}
