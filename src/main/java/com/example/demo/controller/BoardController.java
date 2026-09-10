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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                          Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        List<BoardDTO> boardDTOList;
        if (searchKeyword != null && !searchKeyword.isBlank()) {
            boardDTOList = boardService.searchList(searchKeyword);
        } else {
            boardDTOList = boardService.findAll();
        }
        model.addAttribute("boardList", boardDTOList);
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