package com.photoboothmap.backend.login.authentication.application;

import com.photoboothmap.backend.login.authentication.domain.AuthTokens;
//import com.photoboothmap.backend.login.authentication.infra.google.NaverLoginParams;
import com.photoboothmap.backend.login.authentication.infra.kakao.KakaoLoginParams;
import com.photoboothmap.backend.login.authentication.service.AuthService;
import com.photoboothmap.backend.login.common.dto.LoginDto;
import com.photoboothmap.backend.login.common.dto.SuccessDto;
import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import com.photoboothmap.backend.util.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepository;

    // cookie period: 현재 헤더에서 나타나지 X
/*    @Value("${jwt.cookie-period}")
    private final long COOKIE_EXPIRATION;*/

    @PostMapping("/kakao")
    public ResponseEntity<BaseResponse<LoginDto>> loginKakao(@RequestBody KakaoLoginParams params) {
        // User 등록 및 Refresh Token 저장, 닉네임 가져오는 로직 추가.
        Map<String, AuthTokens> maps = authService.login(params);

        // Map의 key, value 를 통해 토큰과 닉네임을 가져온다.
        AuthTokens tokens = maps.values().stream().findFirst().orElse(null);
        String nickname = maps.keySet().stream().findFirst().orElse(null);

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 닉네임을 통해 profile_image_url를 찾아온다.
        String profile_image_url = member.getProfile_image_url();

        // RT 저장
        HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokens.getRefreshToken())
/*                .maxAge(COOKIE_EXPIRATION)
                .httpOnly(true)
                .secure(true)*/
                .build();


        LoginDto loginDto = LoginDto.builder()
                .nickname(nickname)
                .profile_image_url(profile_image_url)
                .build();


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                // AT 저장
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken())
                .body(new BaseResponse<>(loginDto));
//                .build();
    }

    // 추후 구글로 변경. 전체 disable 처리.
/*    @PostMapping("/naver")
    public ResponseEntity<AuthTokens> loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(authService.login(params));
    }*/

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
        authService.logout(requestAccessToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

}
