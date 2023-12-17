package com.almostThere.domain.chatting.dto;

import com.almostThere.domain.meeting.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ChattingResponseDto {

    // 모임 ID
    private Long meetingId;

    // 모임 코드
//    private String roomCode;

    // 멤버 ID
    private Long memberId;

    // 모임 이름
    private String meetingName;

    // 모임에 해당하는 멤버 리스트
    private Map<Long, ChattingMemberDto> chattingMemberMap;

    public ChattingResponseDto(Meeting meeting) {
        this.meetingId = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.chattingMemberMap = meeting.getMeetingMembers().stream()
                                        .map(m -> new ChattingMemberDto(m.getMember()))
                                        .collect(Collectors.toMap(m -> m.getMemberId(), n -> n));
//        this.roomCode = meeting.getRoomCode();
    }
}
