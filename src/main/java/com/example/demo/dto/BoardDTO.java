package com.example.demo.dto;

import com.example.demo.entity.BoardEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString

public class BoardDTO {
    private Long id;
    private String boardWriter;   //작성자
    private String boardPass;   //비번
    private String updatePass;
    private String boardTitle; //제목
    private String boardContents; //내용
    private int boardHits; //조회수
    private String boardCreatedAt;//작성시간

    private String dateFormat(LocalDateTime date) {
        if(date == null)
            return null;

        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }


    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedAt(boardDTO.dateFormat(boardEntity.getCreatedAt()));  //??좀다름
        return boardDTO;
    }
}
