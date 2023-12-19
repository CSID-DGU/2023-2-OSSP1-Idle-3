package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.Meeting;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingTimeDto {

    private Long meetingId;
    private String meetingTime;

    public MeetingTimeDto(Meeting meeting) {
        this.meetingId = meeting.getId();
        this.meetingTime = meeting.getMeetingTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
