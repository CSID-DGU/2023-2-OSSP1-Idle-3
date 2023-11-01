package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.MeetingCnt;
import com.almostThere.domain.user.dto.MemberDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingCntDto {
    private Long id;
    private MemberDto friend;
    private int cnt;

    public MeetingCntDto(MeetingCnt meetingCnt) {
        this.id = meetingCnt.getId();
        this.friend = new MemberDto(meetingCnt.getFriend());
        this.cnt = meetingCnt.getCnt();
    }
}
