package com.almostThere.domain.chatting.entity;

import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.user.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_id")
    @Comment("채팅 메세지 ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_id", foreignKey = @ForeignKey(name = "fk_chatting_meeting_idx"), nullable = false)
    @Comment("채팅이 있는 모임")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_chatting_member_idx"), nullable = false)
    @Comment("채팅을 작성한 멤버")
    private Member member;

    @Column(nullable = false)
    @Comment("채팅 내용")
    private String message;

    @Column(nullable = false)
    @Comment("채팅 작성 일시")
    private LocalDateTime chattingTime;
}