package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.user.dto.MemberDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class AttendMeetingDto {
    private Long id;
    private String meetingName;
    private String meetingTime;
    private String meetingPlace;
    private String meetingAddress;
    private int roomCode;

    public AttendMeetingDto (Meeting meeting) {
        this.id = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.meetingTime = meeting.getMeetingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.meetingPlace = meeting.getMeetingPlace();
        this.meetingAddress = meeting.getMeetingAddress();
        this.roomCode = Integer.parseInt(meeting.getRoomCode());
    }
}
