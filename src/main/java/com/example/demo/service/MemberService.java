package com.example.demo.service;

import com.example.demo.dto.MemberDTO;
import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void join(MemberDTO memberDTO) {
        // 아이디 중복 체크
        memberRepository.findByMemberId(memberDTO.getMemberId()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        });
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    // 로그인 성공하면 세션에 저장할 MemberDTO(비번 제외)를 반환, 실패하면 예외
    public MemberDTO login(MemberDTO memberDTO) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!memberEntity.getMemberPass().equals(memberDTO.getMemberPass())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return MemberDTO.toMemberDTO(memberEntity);
    }
}