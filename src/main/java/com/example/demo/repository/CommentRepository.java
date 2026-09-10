package com.example.demo.repository;

import com.example.demo.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // "연관된 BoardEntity의 id"로 찾기 -> 필드명(boardEntity) + "_" + 그 안의 필드(Id)
    List<CommentEntity> findByBoardEntity_IdOrderByIdAsc(Long boardId);
}