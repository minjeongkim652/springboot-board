package com.example.demo.entity;

import com.example.demo.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//모델 클래스ㅡ 데이터베이스 테이블을 정의한다

    @Entity
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Table(name="boardEntity")
        public class BoardEntity extends BaseTimeEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column
        private String boardName;

        @Column
        private String boardContents;

        @Column
        private String boardWriter;

        @Column
        private String boardPass;

        @Column
        private String boardTitle;

        @Column
        private int boardHits;
    //dto에 담겨있는 값을 엔티티에 값으로 옮겨담는다?
        public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
            BoardEntity boardEntity = new BoardEntity();
            boardEntity.boardWriter = boardDTO.getBoardWriter();
            boardEntity.boardTitle = boardDTO.getBoardTitle();
            boardEntity.boardPass = boardDTO.getBoardPass();
            boardEntity.boardContents = boardDTO.getBoardContents();
            boardEntity.boardHits = 0;
            return boardEntity;

        }

        public void update(BoardDTO boardDTO) {
            this.boardTitle = boardDTO.getBoardTitle();
            this.boardContents = boardDTO.getBoardContents();
        }
    }


