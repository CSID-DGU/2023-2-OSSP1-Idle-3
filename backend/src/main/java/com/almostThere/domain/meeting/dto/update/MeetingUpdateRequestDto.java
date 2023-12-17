package com.almostThere.domain.meeting.dto.update;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
public class MeetingUpdateRequestDto {

    private Long meetingId;
    private Long hostId;
    private String meetingName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime meetingTime;
    private String meetingPlace;
    private String meetingAddress;
    private double meetingLat;
    private double meetingLng;
    private Integer lateAmount;
    private LocalDateTime regdate;
}
