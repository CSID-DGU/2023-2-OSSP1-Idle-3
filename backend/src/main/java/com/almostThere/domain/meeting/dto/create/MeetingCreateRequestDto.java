package com.almostThere.domain.meeting.dto.create;

import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.user.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MeetingCreateRequestDto {

    private Long hostId;
    private String meetingName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime meetingTime;
    private String meetingPlace;
    private String meetingAddress;
    private double meetingLat;
    private double meetingLng;
    private String roomCode;

    public Meeting toEntity(MeetingCreateRequestDto meetingCreateRequestDto, Member host, String roomCode) {
        return Meeting
            .builder()
            .host(host)
            .meetingName(meetingCreateRequestDto.meetingName)
            .meetingTime(meetingCreateRequestDto.meetingTime)
            .meetingPlace(meetingCreateRequestDto.meetingPlace)
            .meetingAddress(meetingCreateRequestDto.meetingAddress)
            .meetingLat(meetingCreateRequestDto.meetingLat)
            .meetingLng(meetingCreateRequestDto.meetingLng)
            .roomCode(roomCode)
            .build();
    }
}
