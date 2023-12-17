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
public class MeetingDto {
    private Long id;
    private MemberDto host;
    private String meetingName;
    private String meetingTime;
    private String meetingPlace;
    private String meetingAddress;
    private double meetingLat;
    private double meetingLng;
    private Integer lateAmount;
    private String regdate;
    private String roomCode;
    private List<MeetingMemberDto> meetingMembers;
    private List<CalculateDetailDto> calculateDetails;

    public MeetingDto(Meeting meeting) {
        this.id = meeting.getId();
        this.host = new MemberDto(meeting.getHost());
        this.meetingName = meeting.getMeetingName();
        this.meetingTime = meeting.getMeetingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.meetingPlace = meeting.getMeetingPlace();
        this.meetingAddress = meeting.getMeetingAddress();
        this.meetingLat = meeting.getMeetingLat();
        this.meetingLng = meeting.getMeetingLng();
        this.lateAmount = meeting.getLateAmount();
        this.regdate = meeting.getRegdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.roomCode = meeting.getRoomCode();
        this.meetingMembers = meeting.getMeetingMembers()
            .stream().map(m->new MeetingMemberDto(m)).collect(Collectors.toList());
        this.calculateDetails = meeting.getCalculateDetails()
            .stream().map(m->new CalculateDetailDto(m)).collect(Collectors.toList());
    }
}
