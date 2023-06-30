package com.photoboothmap.backend.login.authentication.controller;

import com.photoboothmap.backend.login.authentication.domain.AuthTokens;
//import com.photoboothmap.backend.login.authentication.infra.google.NaverLoginParams;
import com.photoboothmap.backend.login.authentication.infra.kakao.KakaoLoginParams;
import com.photoboothmap.backend.login.authentication.service.AuthService;
import com.photoboothmap.backend.login.dto.LoginDto;
import com.photoboothmap.backend.login.dto.RespLoginDto;
import com.photoboothmap.backend.login.dto.SuccessDto;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepository;

    @PostMapping("/kakao")
    public ResponseEntity<BaseResponse> loginKakao(@RequestBody KakaoLoginParams params) {
        try {
            RespLoginDto respLoginDto = authService.login(params);

            HttpHeaders headers = respLoginDto.getHeaders();
            LoginDto loginDto = respLoginDto.getLoginDto();

            return ResponseEntity.ok().headers(headers).body(new BaseResponse<>(loginDto));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus()).convert();
        }
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {

        log.info("requestAccessToken: {} | requestRefreshToken: {}", requestAccessToken, requestRefreshToken);
        AuthTokens.TokenDto reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);

        SuccessDto successDto;
        if (reissuedTokenDto != null) { // 토큰 재발급 성공
            // RT 저장
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
/*                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)*/
                    .build();
            // success true
            successDto = SuccessDto.builder().success(true).build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    // AT 저장
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                    .body(successDto);
//                    .build();

        } else { // Refresh Token 탈취 가능성
            // Cookie 삭제 후 재로그인 유도
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            // success false
            successDto = SuccessDto.builder().success(false).build();

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(successDto);
//                    .build();
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        try {
            // RT가 null이 아니면서 empty가 아닌 경우 로그아웃 진행.
            if (requestAccessToken != null && !requestAccessToken.isEmpty())
                authService.logout(requestAccessToken);

//        authService.logout(requestAccessToken);
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                    .maxAge(0)
                    .path("/")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

            return ResponseEntity.ok().headers(headers).body(new BaseResponse<>("로그아웃 되었습니다."));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus()).convert();
        }


    }

}
