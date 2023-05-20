package com.photoboothmap.backend.login.member.domain;


import com.photoboothmap.backend.util.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/* DB에서 사용자의 정보를 직접 가져오는 인터페이스. loadUserByUsername(String username)을 오버라이드해 구현했다.
        이 메소드를 사용해 UserDetails를 구현한 UserDetailsImpl 객체를 리턴해 인증 과정에 사용하게 된다.*/

@Component
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user with this email. -> " + email));

        if(findMember != null){
            MemberDetailsImpl userDetails = new MemberDetailsImpl(findMember);
            return  userDetails;
        }

        return null;
    }
}