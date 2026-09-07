package com.example.demo.controller;

import com.example.demo.config.MemberDetails;
import com.example.demo.dto.BoardDTO;
import com.example.demo.service.BoardService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    // 이 URL 자체가 이미 SecurityConfig에서 "로그인 필요"로 막혀있어서,
    // 여기까지 들어왔다는 건 이미 로그인된 상태라는 뜻 -> 별도 체크 코드 필요 없음!
    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/save")
    public String save(BoardDTO boardDTO, @AuthenticationPrincipal MemberDetails memberDetails) {
        // 로그인한 회원의 닉네임을 작성자로 자동 지정
        boardDTO.setBoardWriter(memberDetails.getMemberEntity().getMemberNickname());
        boardService.save(boardDTO);
        return "redirect:/board/";
    }

    @GetMapping("/")
    public String findAll(@RequestParam(required = false) String searchKeyword,
                          Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        List<BoardDTO> boardDTOList;
        if (searchKeyword != null && !searchKeyword.isBlank()) {
            boardDTOList = boardService.searchList(searchKeyword);
        } else {
            boardDTOList = boardService.findAll();
        }
        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("searchKeyword", searchKeyword);
        // 로그인 안 했으면 memberDetails가 null -> 그대로 null을 모델에 넣으면
        // list.html의 th:if="${loginMember != null}" 분기가 예전처럼 그대로 동작함
        model.addAttribute("loginMember", memberDetails != null ? memberDetails.getMemberEntity() : null);
        return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           HttpServletRequest request, HttpServletResponse response,
                           @AuthenticationPrincipal MemberDetails memberDetails) {

        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + id + "]")) {
                boardService.updateHits(id);
                oldCookie.setValue(oldCookie.getValue() + "[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            boardService.updateHits(id);
            Cookie newCookie = new Cookie("boardView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }

        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        model.addAttribute("loginMember", memberDetails != null ? memberDetails.getMemberEntity() : null);
        return "detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "delete";
    }

    @PostMapping("/delete")
    public String delete(BoardDTO boardDTO, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            boardService.delete(boardDTO);
            return "redirect:/board/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/delete/" + boardDTO.getId();
        }
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDTO, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            boardService.update(boardDTO);
            return "redirect:/board/" + boardDTO.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/update/" + boardDTO.getId();
        }
    }
}