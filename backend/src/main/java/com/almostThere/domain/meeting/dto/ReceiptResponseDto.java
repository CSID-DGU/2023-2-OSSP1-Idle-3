package com.almostThere.domain.meeting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceiptResponseDto {
    private String storeName;
    private Integer totalPrice;

}
