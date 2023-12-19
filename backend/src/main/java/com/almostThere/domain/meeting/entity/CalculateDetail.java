package com.almostThere.domain.meeting.entity;

import com.almostThere.domain.meeting.dto.create.CalculateDetailRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalculateDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculate_detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_id", foreignKey = @ForeignKey(name = "calculate_detail_meeting_idx"), nullable = false)
    private Meeting meeting;

    @Enumerated(EnumType.STRING)
    private CalculateType type;

    @Column(length = 300)
    private String filePath;

    @Column(length = 300)
    private String fileName;

    @Column(length = 50)
    private String storeName;

    @Column(nullable = false)
    private int price;

    public CalculateDetail(CalculateDetailRequestDto dto, String filePath, String fileName, Meeting meeting) {
        this.meeting = meeting;
        this.type = CalculateType.RECEIPT;
        this.filePath = filePath;
        this.fileName = fileName;
        this.storeName = dto.getStoreName();
        this.price = dto.getPrice();
    }
}
