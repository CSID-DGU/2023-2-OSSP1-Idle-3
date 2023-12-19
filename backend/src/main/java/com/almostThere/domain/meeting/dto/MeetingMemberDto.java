package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.user.dto.MemberDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingMemberDto {
    private Long id;
    private MemberDto member;
    private String startPlace;
    private String startAddress;
    private Double startLat;
    private Double startLng;
    private StateType state;
    private int spentMoney;

    public MeetingMemberDto(MeetingMember m) {
        this.id = m.getId();
        this.member = new MemberDto(m.getMember());
        this.startPlace = m.getStartPlace();
        this.startAddress = m.getStartAddress();
        this.startLat = m.getStartLat();
        this.startLng = m.getStartLng();
        this.state = m.getState();
        this.spentMoney = m.getSpentMoney();
    }
}
