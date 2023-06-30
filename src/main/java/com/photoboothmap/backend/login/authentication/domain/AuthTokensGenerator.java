package com.photoboothmap.backend.login.authentication.domain;

import com.photoboothmap.backend.login.authentication.infra.jwt.JwtTokenProvider;
import com.photoboothmap.backend.login.authentication.service.AuthService;
import com.photoboothmap.backend.login.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer";

    // 추후 application.yml로.
    @Value("${jwt.access-period}")
    private long ATPeriod;

    @Value("${jwt.refresh-period}")
    private long RTPeriod;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    // 토큰 발급
    public AuthTokens generate(String provider, Long memberId, String email) {
        // RT가 이미 있을 경우
        if(redisService.getValues("RT(" + provider + "):" + email + "") != null) {
            redisService.deleteValues("RT(" + provider + "):" + email); // 삭제
        }

        Date now = new Date();

        Date accessTokenExpiredAt = new Date(now.getTime() + ATPeriod);
        Date refreshTokenExpiredAt = new Date(now.getTime() + RTPeriod);

        log.info("accessTokenExpiredAt: {}", accessTokenExpiredAt);
        log.info("RTPeriod: {}", RTPeriod);
        log.info("ATPeriod: {}", ATPeriod);

        String subject = memberId.toString();
        String accessToken = jwtTokenProvider.generate(email, subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.generate(email, subject, refreshTokenExpiredAt);

        // Redis에 RT 저장
        saveRefreshToken(provider, email, refreshToken);
        log.info("Redis 저장 완료");

        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ATPeriod / 1000L);
    }

    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }

    // 중복 메서드라 처리 필요. -> 합치는 방향 고안해볼 것.
    public void saveRefreshToken(String provider, String principal, String refreshToken) {
        redisService.setValuesWithTimeout("RT(" + provider + "):" + principal, // key
                refreshToken, // value
                jwtTokenProvider.getTokenExpirationTime(refreshToken)); // timeout(milliseconds)
    }
}
