package com.almostThere.domain.meeting.dto.detail;

import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingMemberResponseDto {

    private Long memberId;
    private String memberEmail;
    private String memberNickname;
    private String memberProfileImg;
    private String startPlace;
    private String startAddress;
    private Double startLat;
    private Double startLng;
    private int spentMoney;
    private StateType state;

    public MeetingMemberResponseDto(MeetingMember meetingMember) {
        this.memberId = meetingMember.getMember().getId();
        this.memberEmail = meetingMember.getMember().getMemberEmail();
        this.memberNickname = meetingMember.getMember().getMemberNickname();
        this.memberProfileImg = meetingMember.getMember().getMemberProfileImg();
        this.startPlace = meetingMember.getStartPlace();
        this.startAddress = meetingMember.getStartAddress();
        this.startLat = meetingMember.getStartLat();
        this.startLng = meetingMember.getStartLng();
        this.spentMoney = meetingMember.getSpentMoney();
        this.state = meetingMember.getState();
    }
}
