package com.photoboothmap.backend.login.member.application;

import com.photoboothmap.backend.login.authentication.domain.AuthTokensGenerator;
import com.photoboothmap.backend.login.authentication.service.AuthService;
import com.photoboothmap.backend.login.common.dto.SuccessDto;
import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import com.photoboothmap.backend.util.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;

    @GetMapping
    public ResponseEntity<BaseResponse<List<Member>>> findAll() {
        return ResponseEntity.ok().body(new BaseResponse<>(memberRepository.findAll()));
    }

    @GetMapping("/validate")
    public ResponseEntity<SuccessDto> findByAccessToken(@RequestHeader("Authorization") String requestAccessToken) {
        SuccessDto successDto;

        if (!authService.isValidateRequired(requestAccessToken)) {
            successDto = SuccessDto.builder().success(true).build();
            return ResponseEntity.status(HttpStatus.OK).body(successDto); // 재발급 필요X
        } else {
            successDto = SuccessDto.builder().success(false).build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(successDto); // 재발급 필요
        }
    }
}
