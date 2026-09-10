package com.example.demo.controller;

import com.example.demo.config.MemberDetails;
import com.example.demo.dto.CommentDTO;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public String save(CommentDTO commentDTO, @AuthenticationPrincipal MemberDetails memberDetails) {
        commentDTO.setCommentWriter(memberDetails.getMemberEntity().getMemberNickname());
        commentDTO.setMemberId(memberDetails.getMemberEntity().getMemberId());
        commentService.save(commentDTO);
        return "redirect:/board/" + commentDTO.getBoardId();
    }

    @PostMapping("/update")
    public String update(CommentDTO commentDTO, @AuthenticationPrincipal MemberDetails memberDetails,
                         RedirectAttributes redirectAttributes) {
        try {
            commentService.update(commentDTO, memberDetails.getMemberEntity().getMemberId());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/board/" + commentDTO.getBoardId();
    }

    @PostMapping("/delete")
    public String delete(Long id, Long boardId, @AuthenticationPrincipal MemberDetails memberDetails,
                         RedirectAttributes redirectAttributes) {
        try {
            commentService.delete(id, memberDetails.getMemberEntity().getMemberId());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/board/" + boardId;
    }
}