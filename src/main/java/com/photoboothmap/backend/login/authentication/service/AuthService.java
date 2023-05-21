package com.photoboothmap.backend.login.authentication.service;

import com.photoboothmap.backend.login.authentication.domain.AuthTokens;
import com.photoboothmap.backend.login.authentication.domain.AuthTokensGenerator;
import com.photoboothmap.backend.login.authentication.domain.oauth.OAuthInfoResponse;
import com.photoboothmap.backend.login.authentication.domain.oauth.OAuthLoginParams;
import com.photoboothmap.backend.login.authentication.domain.oauth.RequestOAuthInfoService;
import com.photoboothmap.backend.login.authentication.infra.jwt.JwtTokenProvider;
import com.photoboothmap.backend.login.common.redis.RedisService;
import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    private final String SERVER = "Server";

    // 로그인: 인증 정보 저장 및 비어 토큰 발급
    public Map<String, AuthTokens> login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);

        String nickname = findNickName(oAuthInfoResponse);
        log.info("nickname: {}", nickname);

        Long memberId = findOrCreateMember(oAuthInfoResponse);
//        return authTokensGenerator.generate(SERVER, memberId, oAuthInfoResponse.getEmail());
        return Map.of(nickname, authTokensGenerator.generate(SERVER, memberId, oAuthInfoResponse.getEmail()));
    }

    // RT를 Redis에 저장
    @Transactional
    public void saveRefreshToken(String provider, String principal, String refreshToken) {
        log.info("------------------- redis 저장");
        redisService.setValuesWithTimeout("RT(" + provider + "):" + principal, // key
                refreshToken, // value
                jwtTokenProvider.getTokenExpirationTime(refreshToken)); // timeout(milliseconds)
    }

    // AT가 만료일자만 초과한 유효한 토큰인지 검사 - true일 경우 재발급이 필요.
    public boolean isValidateRequired(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        try {
            Long memberId = authTokensGenerator.extractMemberId(requestAccessToken);
            if (!memberRepository.existsById(memberId)) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return jwtTokenProvider.validateAccessTokenOnlyExpired(requestAccessToken); // true = 재발급
    }

    // 토큰 재발급: validate 메서드가 true 반환할 때만 사용 -> AT, RT 재발급 : 확인 필요.
    @Transactional
    public AuthTokens.TokenDto reissue(String requestAccessTokenInHeader, String requestRefreshToken) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);

        Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
        String subject = jwtTokenProvider.getMemId(requestAccessToken);

        String principal = getPrincipal(requestAccessToken);

        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
        String keyInRedis = ("RT(" + SERVER + "):" + principal);
        if (refreshTokenInRedis == null) { // Redis에 저장되어 있는 RT가 없을 경우
            return null; // -> 재로그인 요청
        }

        // 요청된 RT의 유효성 검사 & Redis에 저장되어 있는 RT와 같은지 비교
        if(!jwtTokenProvider.validateRefreshToken(requestRefreshToken, keyInRedis) || !refreshTokenInRedis.equals(requestRefreshToken)) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal); // 탈취 가능성 -> 삭제
            return null; // -> 재로그인 요청
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 재발급 및 Redis 업데이트
        redisService.deleteValues("RT(" + SERVER + "):" + principal); // 기존 RT 삭제
//        AuthTokens.TokenDto tokenDto = AuthTokensGenerator.reissueGenerate(principal, authorities);

        // 토큰 재발급
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + 1000 * 60 * 30);
        Date refreshTokenExpiredAt = new Date(now + 1000 * 60 * 60 * 24 * 7);

        String accessToken = jwtTokenProvider.generate(principal, subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.generate(principal, subject, refreshTokenExpiredAt);

        AuthTokens.TokenDto tokenDto = new AuthTokens.TokenDto(accessToken, refreshToken);

        saveRefreshToken(SERVER, principal, tokenDto.getRefreshToken());
        return tokenDto;
    }

    // 추가 --
    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private String findNickName(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getNickname)
                .orElse("default");
    }

    private String findProfileImage(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getProfile_image_url)
                .orElse("noImage");
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .profile_image_url(oAuthInfoResponse.getProfileImageUrl())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .role(Member.Role.USER) // 추가.
                .build();

        return memberRepository.save(member).getId();
    }

    // 권한 이름 가져오기
    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    // AT로부터 principal 추출
    public String getPrincipal(String requestAccessToken) {
        return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
    }

    // "Bearer {AT}"에서 {AT} 추출
    public String resolveToken(String requestAccessTokenInHeader) {
        if (requestAccessTokenInHeader == null || !requestAccessTokenInHeader.startsWith("Bearer ")) {
            log.info("에러 발생!");
            throw new IllegalArgumentException("Invalid token in Authorization header");
        }
        return requestAccessTokenInHeader.substring(7);
    }

    // 로그아웃
    @Transactional
    public void logout(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        String principal = getPrincipal(requestAccessToken);

        // Redis에 저장되어 있는 RT 삭제
        String refreshTokenInRedis = redisService.getValues("RT(" + SERVER + "):" + principal);
        if (refreshTokenInRedis != null) {
            redisService.deleteValues("RT(" + SERVER + "):" + principal);
        }

        // Redis에 로그아웃 처리한 AT 저장
        long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
        redisService.setValuesWithTimeout(requestAccessToken,
                "logout",
                expiration);
    }


}
