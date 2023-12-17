package com.almostThere.domain.user.dto;

import com.almostThere.domain.meeting.dto.AttendMeetingMemberDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MemberInfoDto {
    private MemberDto member;

    private List<AttendMeetingMemberDto> attendMeetings;

    // 이번달 모임개수
    private Integer thisMonthAttendMeetingCnt;

    // 총 모임 지각횟수
    private Integer totalLateCnt;

    // 지난달 모임 총 소비가격
    private int lastMonthTotalSpentMoney;

    public MemberInfoDto(
            MemberDto member,
            List<AttendMeetingMemberDto> attendMeetings,
            Integer thisMonthAttendMeetingCnt,
            Integer totalLateCnt,
            int lastMonthTotalSpentMoney) {
        this.member = member;
        this.attendMeetings = attendMeetings;
        this.thisMonthAttendMeetingCnt = thisMonthAttendMeetingCnt;
        this.totalLateCnt = totalLateCnt;
        this.lastMonthTotalSpentMoney = lastMonthTotalSpentMoney;
    }
}