package com.example.demo.controller;

import com.example.demo.config.MemberDetails;
import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/save")
    public String save(BoardDTO boardDTO, @AuthenticationPrincipal MemberDetails memberDetails) {
        boardDTO.setBoardWriter(memberDetails.getMemberEntity().getMemberNickname());
        boardDTO.setMemberId(memberDetails.getMemberEntity().getMemberId()); // 소유권 확인용 아이디 같이 저장
        boardService.save(boardDTO);
        return "redirect:/board/";
    }

    @GetMapping("/")
    public String findAll(@RequestParam(required = false) String searchKeyword,
                          @RequestParam(defaultValue = "0") int page,
                          Model model, @AuthenticationPrincipal MemberDetails memberDetails) {

        // 페이지 크기 10, 정렬 기준은 id 오름차순 (오래된 글이 1번이었던 것과 순서 유지)
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").ascending());

        Page<BoardDTO> boardPage;
        if (searchKeyword != null && !searchKeyword.isBlank()) {
            boardPage = boardService.searchList(searchKeyword, pageable);
        } else {
            boardPage = boardService.findAll(pageable);
        }

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("boardList", boardPage.getContent()); // 화면에 뿌릴 실제 목록
        model.addAttribute("searchKeyword", searchKeyword);
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
        model.addAttribute("commentList", commentService.findByBoardId(id));
        model.addAttribute("newComment", new CommentDTO());
        return "detail";
    }

    // 삭제 확인 화면 - 본인 글 아니면 상세페이지로 돌려보냄
    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable Long id, Model model,
                             @AuthenticationPrincipal MemberDetails memberDetails) {
        BoardDTO boardDTO = boardService.findById(id);
        if (memberDetails == null || !boardDTO.getMemberId().equals(memberDetails.getMemberEntity().getMemberId())) {
            return "redirect:/board/" + id;
        }
        model.addAttribute("board", boardDTO);
        return "delete";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, @AuthenticationPrincipal MemberDetails memberDetails,
                         RedirectAttributes redirectAttributes) {
        try {
            boardService.delete(id, memberDetails.getMemberEntity().getMemberId());
            return "redirect:/board/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/" + id;
        }
    }

    // 수정 화면 - 본인 글 아니면 상세페이지로 돌려보냄
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model,
                             @AuthenticationPrincipal MemberDetails memberDetails) {
        BoardDTO boardDTO = boardService.findById(id);
        if (memberDetails == null || !boardDTO.getMemberId().equals(memberDetails.getMemberEntity().getMemberId())) {
            return "redirect:/board/" + id;
        }
        model.addAttribute("board", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDTO, @AuthenticationPrincipal MemberDetails memberDetails,
                         RedirectAttributes redirectAttributes) {
        try {
            boardService.update(boardDTO, memberDetails.getMemberEntity().getMemberId());
            return "redirect:/board/" + boardDTO.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/" + boardDTO.getId();
        }
    }
}