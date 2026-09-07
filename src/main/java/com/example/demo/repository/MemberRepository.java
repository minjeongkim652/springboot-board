package com.example.demo.repository;

import com.example.demo.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 로그인할 때 아이디로 회원 찾기
    Optional<MemberEntity> findByMemberId(String memberId);
}