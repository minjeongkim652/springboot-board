package com.example.demo.repository;

import com.example.demo.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

  @Modifying
  @Query(value = "update BoardEntity b set b.boardHits = b.boardHits + 1 where b.id = :id")
  void updateHits(@Param("id") Long id);

  // Pageable을 파라미터에 추가하면 반환 타입도 Page<T>로 바뀌면서 자동으로 페이징 처리됨
  Page<BoardEntity> findByBoardTitleContainingOrBoardWriterContaining(
          String boardTitle, String boardWriter, Pageable pageable);
}