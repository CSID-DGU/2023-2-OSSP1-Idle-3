package com.almostThere.domain.meeting.dto.update;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class MeetingStartPlaceRequestDto {

    private Long meetingId;

    private Long memberId;

    private String startPlace;

    private String startAddress;

    private Double startLat;

    private Double startLng;
}
