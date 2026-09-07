
package com.example.demo.dto;

import com.example.demo.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

// 세션에 저장할 거라 Serializable 구현 (세션 저장 객체는 직렬화 가능해야 함)
@Getter
@Setter
@NoArgsConstructor
public class MemberDTO implements Serializable {
    private Long id;
    private String memberId;
    private String memberPass;
    private String memberNickname;

    // 로그인 성공 후 세션에 저장할 때 씀 — 비밀번호는 일부러 안 담음 (세션에 비번 남기지 않기 위함)
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setMemberNickname(memberEntity.getMemberNickname());
        return memberDTO;
    }
}

