package com.photoboothmap.backend.login.common.dto;

import com.photoboothmap.backend.login.authentication.domain.AuthTokens;
import com.photoboothmap.backend.login.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {
    private String nickname;
    private String profileImageUrl;
    private Long userId;

    public LoginDto(String nickname, String profileImageUrl, Long userId) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.userId = userId;
    }
}

