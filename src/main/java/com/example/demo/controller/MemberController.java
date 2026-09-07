package com.example.demo.controller;

import com.example.demo.dto.MemberDTO;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/member/join")
    public String join(MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
        try {
            memberService.join(memberDTO);
            return "redirect:/member/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/member/join";
        }
    }

    // 로그인 화면 "보여주기"는 여전히 우리 몫 (Security는 화면 렌더링은 안 해줌)
    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    // 로그인 POST 처리, 로그아웃 처리는 이제 SecurityConfig가 자동으로 가로채서 처리하니까
    // 여기엔 더 이상 관련 메서드가 없음! (예전 login(), logout() 메서드 삭제됨)
}