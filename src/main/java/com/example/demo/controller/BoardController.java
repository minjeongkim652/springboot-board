package com.example.demo.controller;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.MemberDTO;
import com.example.demo.service.BoardService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    // 로그인 안 했으면 글쓰기 폼 자체를 못 열게 막음
    @GetMapping("/save")
    public String saveForm(HttpSession session) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/member/login";
        }
        return "save";
    }

    // 저장할 때도 다시 한번 로그인 확인 + 작성자는 세션 닉네임으로 자동 설정
    @PostMapping("/save")
    public String save(BoardDTO boardDTO, HttpSession session) {
        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/member/login";
        }
        boardDTO.setBoardWriter(loginMember.getMemberNickname());
        boardService.save(boardDTO);
        return "redirect:/board/";
    }

    // 목록 + 검색 + 로그인 상태 전달
    @GetMapping("/")
    public String findAll(@RequestParam(required = false) String searchKeyword,
                          Model model, HttpSession session) {
        List<BoardDTO> boardDTOList;
        if (searchKeyword != null && !searchKeyword.isBlank()) {
            boardDTOList = boardService.searchList(searchKeyword);
        } else {
            boardDTOList = boardService.findAll();
        }
        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("loginMember", session.getAttribute("loginMember"));
        return "list";
    }

    // 상세보기 + 조회수 중복 방지 쿠키
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           HttpServletRequest request, HttpServletResponse response,
                           HttpSession session) {

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
        model.addAttribute("loginMember", session.getAttribute("loginMember"));
        return "detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "delete";
    }

    @PostMapping("/delete")
    public String delete(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
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
    public String update(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        try {
            boardService.update(boardDTO);
            return "redirect:/board/" + boardDTO.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/update/" + boardDTO.getId();
        }
    }
}