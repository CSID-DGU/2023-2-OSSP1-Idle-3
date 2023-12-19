package com.almostThere.domain.meeting.dto.detail;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingDetailRequestDto {

    private Long memberId;

    private Long meetingId;
}
