package com.example.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.sound.sampled.AudioInputStream;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AudioInputStream.class)
public abstract class BaseTimeEntity {
    @CreatedDate
    @Column(updatable=false)
    private LocalDateTime createdAt;  //생성시간

    @LastModifiedDate
    private LocalDateTime updatedAt;    //수정시간

}
