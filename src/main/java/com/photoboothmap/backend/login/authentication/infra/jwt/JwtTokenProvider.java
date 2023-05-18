package com.photoboothmap.backend.login.authentication.infra.jwt;

import com.photoboothmap.backend.login.common.redis.RedisService;
import com.photoboothmap.backend.login.member.domain.Member;
import com.photoboothmap.backend.login.member.domain.MemberDetailsImpl;
import com.photoboothmap.backend.login.member.domain.MemberDetailsServiceImpl;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class
JwtTokenProvider {

    private final Key key;
    private final RedisService redisService;
    private final MemberDetailsServiceImpl memberDetailsService;
    private final MemberRepository memberRepository;
    private static final String EMAIL_KEY = "email";

    // 시크릿 키 설정
    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey, RedisService redisService, MemberDetailsServiceImpl memberDetailsService, MemberRepository memberRepository) {
        this.redisService = redisService;
        this.memberDetailsService = memberDetailsService;
        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성 - uid(email) 추가.
    public String generate(String email, String subject, Date expiredAt) {
        // claim 에 email 정보 추가
        Claims claims = Jwts.claims().setSubject(subject); //id로 claim

        // claim 에 권한 정보 추가: subject는 id 값이므로, 다시 long 형변환하여 파싱.
//         Member member = memberRepository.findById(Long.parseLong(subject)).orElseThrow(()-> new RuntimeException());
        Member member = memberRepository.findById(Long.parseLong(subject)).orElse(null);
        if(member != null){
            claims.put("role", member.getRole());
            log.info("----------------- role: {}", member.getRole());
        }
/*         claims.put("role", member.getRole());
         log.info("----------------- role: {}", member.getRole());*/

        log.info("------------subject: {}", subject);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setSubject(subject)
                .setExpiration(expiredAt)
                .claim(EMAIL_KEY, email)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    // 토큰으로부터 정보 추출
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // "Bearer {AT}"에서 {AT} 추출
    public String resolveToken(String requestAccessTokenInHeader) {
        if (requestAccessTokenInHeader == null || !requestAccessTokenInHeader.startsWith("Bearer ")) {
            log.info("에러 발생!");
            throw new IllegalArgumentException("Invalid token in Authorization header");
        }
        return requestAccessTokenInHeader.substring(7);

    }

    public long getTokenExpirationTime(String token) {
        return parseClaims(token).getExpiration().getTime();
    }

    public Authentication getAuthentication(String token) {
        String email = parseClaims(token).get(EMAIL_KEY).toString();
        MemberDetailsImpl userDetailsImpl = memberDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetailsImpl, "", userDetailsImpl.getAuthorities());
    }

    // 토큰 검증 로직
    public boolean validateRefreshToken(String refreshToken) {
        log.info("redisService.getValues(refreshToken): {}", redisService.getValues(refreshToken));
        try {
            if (redisService.getValues(refreshToken).equals("delete")) { // 회원 탈퇴했을 경우
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        } catch (NullPointerException e) {
            log.error("JWT Token is empty.");
        }
        return false;
    }

    // Filter에서 사용
    public boolean validateAccessToken(String accessToken) {
        try {
            if (redisService.getValues(accessToken) != null // NPE 방지
                    && redisService.getValues(accessToken).equals("logout")) { // 로그아웃 했을 경우
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 재발급 검증 API에서 사용
    public boolean validateAccessTokenOnlyExpired(String accessToken) {
        try {
            return parseClaims(accessToken)
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

/*    private Date getExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
    }*/

    public String getMemId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

}

