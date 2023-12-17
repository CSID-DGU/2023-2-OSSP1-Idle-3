package com.almostThere.domain.chatting.dto;

import com.almostThere.domain.chatting.entity.Chatting;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChattingDto implements Serializable {

    private static final long serialVersionUID = 2L;

    // 채팅 입력한 멤버 ID
    private Long memberId;
    
    // 채팅 내용
    private String message;
    
    // 채팅 입력 일시
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime chattingTime;

    public ChattingDto(Chatting chatting) {
        this.memberId = chatting.getMember().getId();
        this.message = chatting.getMessage();
        this.chattingTime = chatting.getChattingTime();
    }
}
