package com.almostThere.domain.meeting.dto;

import com.almostThere.domain.meeting.entity.CalculateDetail;
import com.almostThere.domain.meeting.entity.CalculateType;
import com.almostThere.domain.meeting.entity.Meeting;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CalculateDetailDto {
    private Long id;
    private Long meetingId;
    private CalculateType type;
    private String filePath;
    private String fileName;
    private String storeName;
    private int price;

    public CalculateDetailDto(CalculateDetail c) {
        this.id = c.getId();
        this.meetingId = c.getMeeting().getId();
        this.type = c.getType();
        this.filePath = c.getFilePath();
        this.fileName = c.getFileName();
        this.storeName = c.getStoreName();
        this.price = c.getPrice();
    }
}
