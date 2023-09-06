package com.photoboothmap.backend.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RespReissueDto {
    private String accessToken;
    private Boolean success;

    public RespReissueDto(String accessToken, Boolean success) {
        this.accessToken = accessToken;
        this.success = success;
    }
}
