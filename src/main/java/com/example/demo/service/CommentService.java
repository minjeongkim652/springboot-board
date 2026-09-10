package com.example.demo.service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.CommentEntity;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public void save(CommentDTO commentDTO) {
        BoardEntity boardEntity = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
        CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
        commentRepository.save(commentEntity);
    }

    public List<CommentDTO> findByBoardId(Long boardId) {
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntity_IdOrderByIdAsc(boardId);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            commentDTOList.add(CommentDTO.toCommentDTO(commentEntity));
        }
        return commentDTOList;
    }

    @Transactional
    public void update(CommentDTO commentDTO, String currentMemberId) {
        CommentEntity commentEntity = commentRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        if (!commentEntity.getMemberId().equals(currentMemberId)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
        commentEntity.update(commentDTO.getCommentContents()); // 더티 체킹으로 자동 UPDATE
    }

    public void delete(Long id, String currentMemberId) {
        CommentEntity commentEntity = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        if (!commentEntity.getMemberId().equals(currentMemberId)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.deleteById(id);
    }
}