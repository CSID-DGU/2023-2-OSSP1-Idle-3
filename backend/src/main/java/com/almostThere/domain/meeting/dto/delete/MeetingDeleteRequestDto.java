package com.almostThere.domain.meeting.dto.delete;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class MeetingDeleteRequestDto {

    private Long memberId;

    private Long meetingId;
}
