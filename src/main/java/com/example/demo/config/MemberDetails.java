package com.example.demo.config;

import com.example.demo.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Security는 회원 정보를 이 "UserDetails" 규격으로만 이해할 수 있어서,
// 우리 MemberEntity를 그 규격에 맞게 감싸주는 어댑터 역할
public class MemberDetails implements UserDetails {
    private final MemberEntity memberEntity;

    public MemberDetails(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    // 나중에 컨트롤러에서 로그인한 회원 원본 정보(닉네임 등) 꺼내 쓸 때 사용
    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 등급 (지금은 회원이면 다 동일 권한이라 "USER" 하나만)
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return memberEntity.getMemberPass(); // 암호화된 값 그대로 반환 (Security가 알아서 비교함)
    }

    @Override
    public String getUsername() {
        return memberEntity.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
