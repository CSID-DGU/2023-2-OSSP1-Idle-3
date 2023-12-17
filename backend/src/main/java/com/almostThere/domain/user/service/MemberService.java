package com.almostThere.domain.user.service;

import com.almostThere.domain.meeting.dto.MeetingCntDto;
import com.almostThere.domain.meeting.entity.MeetingCnt;
import com.almostThere.domain.meeting.repository.MeetingCntRepository;
import com.almostThere.domain.user.dto.MemberDto;
import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MeetingCntRepository meetingCntRepository;
    private final MemberRepository memberRepository;

    @Qualifier("redisTemplateForToken")
    private final RedisTemplate redisTemplateForToken;

    public List<MeetingCntDto> findBestMember(Long memberId) {
        Member member = memberRepository.findByid(memberId);
        Page<MeetingCnt> bestMember = meetingCntRepository.findBymyMemberOrderByCntDesc(member,
            PageRequest.of(0, 9));
        List<MeetingCntDto> result = bestMember.stream().map(m -> new MeetingCntDto(m))
            .collect(Collectors.toList());
        return result;
    }

    /**
     * 회원 ID로 Member 객체 가져오기
     *
     * @param memberId
     * @return Member 회원 Entity
     */
    public MemberDto getMemberByMemberId(Long memberId) {
        return new MemberDto(memberRepository.findByid(memberId));
    }

    public Cookie logoutMemberById(Long memberId, Cookie[] cookies) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
            new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Cookie refreshTokenCookie = deleteCookie(cookies);
        redisTemplateForToken.delete(member.getMemberEmail());

        String refreshToken2 = (String) redisTemplateForToken.opsForValue().get(member.getMemberEmail());
//        if(!refreshToken2.equals(token.getRefreshToken())) {
//            throw new CustomException("Refresh Token doesn't match.", HttpStatus.BAD_REQUEST);
//        }

        log.info("redis rt {}", refreshToken2);

        return refreshTokenCookie;
    }

    public Cookie deleteCookie(Cookie[] cookies) {
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info(String.valueOf(cookie.getName()));
                if (cookie.getName().equals("refresh-token")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        Cookie refreshTokenCookie = new Cookie("refresh-token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/api/token/tokenReissue");

        return refreshTokenCookie;
    }
}
