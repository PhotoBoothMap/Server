package com.photoboothmap.backend.login.member.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/*유저의 정보를 가져오는 UserDetails 인터페이스를 상속하는 클래스이다. Authentication을 담고 있다.
        user.getRole().getKey()를 통해 사용자의 권한(Authorities)를 부여해 가져올 수 있다.
        Principal과 Credential로 사용할 필드를 각각 User.email, User.password로 정해두었다.
        세부 설정은 현재로써 필요하지 않기 때문에 true로 반환하게만 해두었다.*/

public class MemberDetailsImpl implements UserDetails {

    private final Member member;

    public MemberDetailsImpl(Member member) {
        this.member = member;
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // == 세부 설정 == //

    @Override
    public boolean isAccountNonExpired() { // 계정의 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정의 잠김 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled() { // 계정의 활성화 여부
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> member.getRole().getKey()); // key: ROLE_권한
        return authorities;
    }

    // disable
/*    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(member.getRole().toString());
        return authorityList;
    }*/
}
