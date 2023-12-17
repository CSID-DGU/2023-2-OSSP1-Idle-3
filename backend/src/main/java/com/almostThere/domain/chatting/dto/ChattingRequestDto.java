package com.almostThere.domain.chatting.dto;

import lombok.Getter;

@Getter
public class ChattingRequestDto {
    
    // 메세지 내용
    String message;

    // 멤버 ID
    Long memberId;
}
