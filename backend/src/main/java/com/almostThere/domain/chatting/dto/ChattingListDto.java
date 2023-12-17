package com.almostThere.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ChattingListDto {
    
    // Chatting 관련 상세 정보 List
    private List<ChattingDto> chattingDtoList;
    
    // 위 리스트에 포함된 Chatting의 index 최솟값
    private Long lastNumber;
}
