package com.example.demo.service;

import com.example.demo.config.MemberDetails;
import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    // Security가 로그인 폼 제출되면 자동으로 이 메서드를 호출함
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다."));
        return new MemberDetails(memberEntity);
    }
}