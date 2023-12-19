package com.almostThere.domain.map.dto;

import com.almostThere.domain.chatting.entity.Chatting;
import com.almostThere.domain.meeting.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
public class MapResponseDto {
    
    // 모임 이름
    private String meetingName;
    
    // 모임 시간
    private LocalDateTime meetingTime;

    // 모임 장소 x
    private double meetingLat;

    // 모임 장소 y
    private double meetingLng;
    
    // 최근 채팅
    private Map<Long, String> chattingDtoMap;

    // 멤버ID
    private Long memberId;

    public MapResponseDto(Meeting meeting, List<Chatting> chattingList) {
        this.meetingName = meeting.getMeetingName();
        this.meetingTime = meeting.getMeetingTime();
        this.meetingLat = meeting.getMeetingLat();
        this.meetingLng = meeting.getMeetingLng();
        this.chattingDtoMap = chattingList.stream().collect(Collectors.toMap((n) -> n.getMember().getId(), (m) -> m.getMessage()));
    }
}