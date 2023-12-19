package com.almostThere.domain.meeting.dto.detail;

import com.almostThere.domain.meeting.entity.Meeting;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class MeetingDetailResponseDto {

    private Long hostId;
    private Long meetingId;
    private String meetingName;
    private String meetingPlace;
    private LocalDateTime meetingTime;
    private String meetingAddress;
    private double meetingLat;
    private double meetingLng;
    private Integer lateAmount;
    private LocalDateTime regDate;
    private String roomCode;
    private int remain;
    private List<MeetingMemberResponseDto> meetingMembers;
    private List<MeetingCalculateDetailDto> calculateDetails;

    public MeetingDetailResponseDto(Meeting meeting, int remain
        , List<MeetingMemberResponseDto> meetingMembers, List<MeetingCalculateDetailDto> calculateDetails) {
        this.hostId = meeting.getHost().getId();
        this.meetingId = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.meetingPlace = meeting.getMeetingPlace();
        this.meetingTime = meeting.getMeetingTime();
        this.meetingAddress = meeting.getMeetingAddress();
        this.meetingLat = meeting.getMeetingLat();
        this.meetingLng = meeting.getMeetingLng();
        this.lateAmount = meeting.getLateAmount();
        this.regDate = meeting.getRegdate();
        this.roomCode = meeting.getRoomCode();
        this.remain = remain;
        this.meetingMembers = meetingMembers;
        this.calculateDetails = calculateDetails;
    }
}
