package com.almostThere.domain.user.dto;

import com.almostThere.domain.user.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String memberEmail;
    private String memberProfileImg;
    private String memberNickname;
    private String regdate;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.memberEmail = member.getMemberEmail();
        this.memberProfileImg = member.getMemberProfileImg();
        this.memberNickname = member.getMemberNickname();
        this.regdate = member.getRegdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
