package com.example.demo.entity;

import com.example.demo.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_entity")
@Getter
@Setter
@NoArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 여러 개(Many) : 게시글 하나(One) -> @ManyToOne
    // DB에는 comment_entity 테이블에 board_id라는 외래키(FK) 컬럼이 생김
    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    private String commentWriter;  // 댓글 작성자 닉네임 (화면 표시용)
    private String memberId;       // 댓글 작성자 로그인 아이디 (소유권 확인용)

    @Lob
    private String commentContents;

    private LocalDateTime commentCreatedAt;

    public static CommentEntity toSaveEntity(CommentDTO commentDTO, BoardEntity boardEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setBoardEntity(boardEntity);
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setMemberId(commentDTO.getMemberId());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setCommentCreatedAt(LocalDateTime.now());
        return commentEntity;
    }

    public void update(String contents) {
        this.commentContents = contents;
    }
}