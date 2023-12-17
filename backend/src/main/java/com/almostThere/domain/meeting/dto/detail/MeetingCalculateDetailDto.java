package com.almostThere.domain.meeting.dto.detail;

import com.almostThere.domain.meeting.entity.CalculateDetail;
import com.almostThere.domain.meeting.entity.CalculateType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeetingCalculateDetailDto {

    private Long calculateDetailId;
    private String fileName;
    private Long meetingId;
    private CalculateType type;
    private String storeName;
    private int price;


    public MeetingCalculateDetailDto(CalculateDetail calculateDetail) {
        this.calculateDetailId = calculateDetail.getId();
        this.fileName = calculateDetail.getFileName();
        this.meetingId = calculateDetail.getMeeting().getId();
        this.type = calculateDetail.getType();
        this.storeName = calculateDetail.getStoreName();
        this.price = calculateDetail.getPrice();
    }
}
