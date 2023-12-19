package com.almostThere.domain.meeting.entity;

import com.almostThere.domain.user.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class MeetingCnt {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_cnt_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "my_member_id", foreignKey = @ForeignKey(name = "fk_meeting_cnt_member_idx1"), nullable = false)
    private Member myMember;

    @ManyToOne
    @JoinColumn(name = "friend_id", foreignKey = @ForeignKey(name = "fk_meeting_cnt_member_idx2"), nullable = false)
    private Member friend;

    @Column(columnDefinition = "int default 1")
    private Integer cnt;

    public MeetingCnt(Member myMember, Member friend) {
        this.myMember = myMember;
        this.friend = friend;
    }

    public void updateCnt(){
        this.cnt += 1;
    }
}
