package com.example.demo.dto;

import com.example.demo.entity.CommentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private Long boardId;          // 어느 게시글에 달린 댓글인지 (폼에서 hidden으로 넘어옴)
    private String commentWriter;
    private String memberId;
    private String commentContents;
    private LocalDateTime commentCreatedAt;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setBoardId(commentEntity.getBoardEntity().getId()); // 연관관계 타고 들어가서 게시글 id 꺼내기
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setMemberId(commentEntity.getMemberId());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedAt(commentEntity.getCommentCreatedAt());
        return commentDTO;
    }
}