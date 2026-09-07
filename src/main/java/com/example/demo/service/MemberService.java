package com.example.demo.service;

import com.example.demo.dto.MemberDTO;
import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // Step 2에서 등록해둔 빈, 생성자로 자동 주입됨

    public void join(MemberDTO memberDTO) {
        memberRepository.findByMemberId(memberDTO.getMemberId()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        });

        // 평문 비번을 암호화한 값으로 바꿔치기 한 다음 저장
        String encodedPass = passwordEncoder.encode(memberDTO.getMemberPass());
        memberDTO.setMemberPass(encodedPass);

        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    // (임시) 지금 단계까지는 우리가 만든 로그인도 같이 살려둠 -> Step 4에서 Security 방식으로 교체 예정
    public MemberDTO login(MemberDTO memberDTO) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        // 평문끼리 비교(equals) 대신, "이 평문을 해싱하면 저장된 해시값과 같은가?"로 검증
        if (!passwordEncoder.matches(memberDTO.getMemberPass(), memberEntity.getMemberPass())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return MemberDTO.toMemberDTO(memberEntity);
    }
}