package com.photoboothmap.backend.mypage.controller;

import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import com.photoboothmap.backend.mypage.dto.resp.RespReviewListDto;
import com.photoboothmap.backend.mypage.service.MypageService;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;
    private final MemberRepository memberRepository;

    @GetMapping("/member")
    public ResponseEntity<BaseResponse<List<Member>>> findAll() {
        return ResponseEntity.ok().body(new BaseResponse<>(memberRepository.findAll()));
    }

    @GetMapping("/mypage")
    public ResponseEntity<BaseResponse> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        try {
            RespReviewListDto reviewDto = mypageService.getReview(userEmail);

            return new BaseResponse<>(reviewDto).convert();
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus()).convert();
        }

    }

}
