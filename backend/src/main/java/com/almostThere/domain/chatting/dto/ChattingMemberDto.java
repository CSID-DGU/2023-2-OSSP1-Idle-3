package com.almostThere.domain.chatting.dto;

import com.almostThere.domain.user.entity.Member;
import lombok.Getter;

@Getter
public class ChattingMemberDto {

    // 사용자 ID
    private Long memberId;

    // 프로필 사진
    private String profile;
    
    // 닉네임
    private String nickname;

    public ChattingMemberDto(Member member) {
        this.memberId = member.getId();
        this.profile = member.getMemberProfileImg();
        this.nickname = member.getMemberNickname();
    }
}