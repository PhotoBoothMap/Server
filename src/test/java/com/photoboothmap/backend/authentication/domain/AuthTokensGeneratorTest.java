package com.photoboothmap.backend.authentication.domain;

import com.photoboothmap.backend.login.authentication.domain.AuthTokens;
import com.photoboothmap.backend.login.authentication.domain.AuthTokensGenerator;
import com.photoboothmap.backend.login.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthTokensGeneratorTest {

    @Autowired
    private AuthTokensGenerator authTokensGenerator;
    private final String SERVER = "Server";
    private MemberRepository memberRepository;

    AuthTokensGeneratorTest() {
    }


    @Test
    @DisplayName("JWT 토큰 생성 성공")
    void testGenerate() {
        // given
        Long memberId = 0L;
        String email = "test@naver.com";

        // when
        AuthTokens authTokens = authTokensGenerator.generate(SERVER, memberId, email);

        // then
        assertThat(authTokens.getGrantType()).isEqualTo("Bearer");
        assertThat(authTokens.getAccessToken()).isNotBlank();
        assertThat(authTokens.getRefreshToken()).isNotBlank();
        assertThat(authTokens.getExpiresIn()).isNotNull();
    }

    @Test
    @DisplayName("JWT 토큰 검증 성공")
    void testExtractSubject() {
        // given
        Long memberId = 0L;
        String email = "test@naver.com";

        AuthTokens authTokens = authTokensGenerator.generate(SERVER, memberId, email);
        String accessToken = authTokens.getAccessToken();

        // when
        Long extractedMemberId = authTokensGenerator.extractMemberId(accessToken);

        // then
        assertThat(extractedMemberId).isEqualTo(memberId);
    }
}
