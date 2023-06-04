package com.photoboothmap.backend.login.authentication.domain.oauth;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    String getProfileImageUrl();
    OAuthProvider getOAuthProvider();
}
