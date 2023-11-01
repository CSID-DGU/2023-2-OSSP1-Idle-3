package com.almostThere.domain.meeting.entity;

import com.almostThere.domain.chatting.entity.Chatting;
import com.almostThere.domain.meeting.dto.update.MeetingUpdateRequestDto;
import com.almostThere.domain.user.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id", foreignKey = @ForeignKey(name = "fk_meeting_member_idx"), nullable = false)
    private Member host;

    @Column(length = 30, nullable = false)
    private String meetingName;

    @Column(nullable = false)
    private LocalDateTime meetingTime;

    @Column(length = 100, nullable = false)
    private String meetingPlace;

    @Column(length = 200, nullable = false)
    private String meetingAddress;

    @Column
    private double meetingLat;

    @Column
    private double meetingLng;

    @Column
    private Integer lateAmount;

    @CreationTimestamp // DB에 insert할 때 현재시간 자동 지정
    @Column(nullable = false)
    private LocalDateTime regdate;

    @Column(nullable = false, length = 10)
    private String roomCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meeting")
    private List<MeetingMember> meetingMembers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meeting")
    private List<CalculateDetail> calculateDetails = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meeting")
    private List<Chatting> chattingList = new ArrayList<>();

    @Builder
    public Meeting(Member host, String meetingName,String meetingPlace,String meetingAddress
        , double meetingLat,double meetingLng, LocalDateTime meetingTime, String roomCode, Integer lateAmount, LocalDateTime regdate) {
        this.meetingName = meetingName;
        this.meetingTime = meetingTime;
        this.meetingPlace = meetingPlace;
        this.meetingAddress = meetingAddress;
        this.meetingLat = meetingLat;
        this.meetingLng = meetingLng;
        this.host = host;
        this.roomCode = roomCode;
        this.lateAmount = lateAmount;
        this.regdate = regdate;
    }

    public void updateMeeting(MeetingUpdateRequestDto meetingUpdateRequestDto) {
        this.meetingName = meetingUpdateRequestDto.getMeetingName();
        this.meetingTime = meetingUpdateRequestDto.getMeetingTime();
        this.meetingPlace = meetingUpdateRequestDto.getMeetingPlace();
        this.meetingAddress = meetingUpdateRequestDto.getMeetingAddress();
        this.meetingLat = meetingUpdateRequestDto.getMeetingLat();
        this.meetingLng = meetingUpdateRequestDto.getMeetingLng();
        this.lateAmount = meetingUpdateRequestDto.getLateAmount();;
        this.regdate = LocalDateTime.now();
    }
}
