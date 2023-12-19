package com.almostThere.domain.meeting.entity;

import com.almostThere.domain.meeting.dto.update.MeetingStartPlaceRequestDto;
import com.almostThere.domain.user.entity.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingMember {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "meeting_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_meeting_member_member_idx"), nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "meeting_id", foreignKey = @ForeignKey(name = "fk_meeting_member_meeting_idx"), nullable = false)
    private Meeting meeting;

    @Column(length = 100)
    private String startPlace;

    @Column(length = 200)
    private String startAddress;

    @Column
    private Double startLat;

    @Column
    private Double startLng;

    @Enumerated(EnumType.STRING)
    private StateType state;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int spentMoney;

    public MeetingMember (Member member, Meeting meeting, StateType state){
        this.member = member;
        this.meeting = meeting;
        this.state = state;
    }
    @Builder
    public MeetingMember (Member member, Meeting meeting, String startPlace, String startAddress
        ,Double startLat, Double startLng, StateType state, int spentMoney) {
        this.member = member;
        this.meeting = meeting;
        this.startPlace = startPlace;
        this.startAddress = startAddress;
        this.startLat = startLat;
        this.startLng = startLng;
        this.state = state;
        this.spentMoney = spentMoney;
    }

    public void updateState(StateType stateType) {
        this.state = stateType;
    }

    public void updateSpentMoney(int spentMoney){
        this.spentMoney = spentMoney;
    }

    /* 출발장소 update */
    public void updateStartPlace(MeetingStartPlaceRequestDto meetingStartPlaceRequestDto) {
        this.startPlace = meetingStartPlaceRequestDto.getStartPlace();
        this.startAddress = meetingStartPlaceRequestDto.getStartAddress();
        this.startLat = meetingStartPlaceRequestDto.getStartLat();
        this.startLng = meetingStartPlaceRequestDto.getStartLng();
    }
}
