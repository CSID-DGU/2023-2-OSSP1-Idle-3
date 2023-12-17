package com.almostThere.domain.meeting.dto.create;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class CalculateDetailRequestDto {
    private Long meetingId;
    private MultipartFile receipt;
    private String storeName;
    private int price;
}
