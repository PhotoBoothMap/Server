package com.photoboothmap.backend.login.member.domain;

import com.photoboothmap.backend.login.authentication.domain.oauth.OAuthProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;

    // 임시 credential
    private String password;

    //role 추가.
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) //저장될때는 string으로 저장되도록
    private Role role;

    // 프로필 이미지 url 추가.
    @Column
    private String profileImageUrl;

    private OAuthProvider oAuthProvider;

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

        private final String key;
    }

    @Builder
    public Member(String email, String nickname, String profileImageUrl, Role role, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.oAuthProvider = oAuthProvider;
    }

    public Member update(String name, String picture){
        this.nickname = name;
        this.profileImageUrl = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }

}
