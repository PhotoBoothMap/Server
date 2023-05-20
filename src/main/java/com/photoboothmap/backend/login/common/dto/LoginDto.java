package com.photoboothmap.backend.login.common.dto;

import com.photoboothmap.backend.login.authentication.domain.AuthTokens;
import com.photoboothmap.backend.login.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {
    private String nickname;
    private String profile_image_url;

    public LoginDto(String nickname, String profile_image_url) {
        this.nickname = nickname;
        this.profile_image_url = profile_image_url;
    }
}

