package com.almostThere.domain.chatting.controller;

import com.almostThere.domain.chatting.dto.*;
import com.almostThere.domain.chatting.service.ChattingService;
import com.almostThere.domain.user.dto.MemberAccessDto;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    /**
     * jeey0124
     * @param meetingId 모임 ID
     * @param chattingRequestDto 채팅 내용 및 작성자
     * @return 보낸 메시지 정보를 반환한다.
     * **/
    @MessageMapping("/receive/{meetingId}") // 메시지를 받을 endpoint 설정
    @SendTo("/send/{meetingId}") // 메시지를 보낼 곳 설정
    public BaseResponse sendChatting(@DestinationVariable String meetingId, ChattingRequestDto chattingRequestDto) {

        String message = chattingRequestDto.getMessage();
        // 메세지 내용은 최대 255자 이하
        if (message.length() <= 255) {

            // 사용자ID 임시값 (Header로 받아오는 거 제대로 되는지 확인 필요)
//            Long memberId = 1L;
            // 우선 memberId는 프론트에서 받아오는 걸로
            Long memberId = chattingRequestDto.getMemberId();

            // 현재 시간 가져오기
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

            // redis에 저장
            ChattingDto chattingDto = chattingService.addChattingRedis(memberId, meetingId, message, now);

            // 반환
            return BaseResponse.success(chattingDto);
        }
        return BaseResponse.fail();
    }

    /**
     * jeey0124
     * @return 입장한 사용자 정보를 반환한다.
     * **/
    @MessageMapping("/welcome/{meetingId}") // 메시지를 받을 endpoint 설정
    @SendTo("/enter/{meetingId}") // 모임 입장
    public BaseResponse enterMeeting(Long memberId) {

        // 사용자ID
        // 우선 memberId는 프론트에서 받아오는 걸로

        // 사용자 프로필, 닉네임 가져오기
        ChattingMemberDto chattingMemberDto = chattingService.getChattingMember(memberId);
        return BaseResponse.success(chattingMemberDto);
    }

    /**
     * jeey0124
     * @param meetingId 미팅ID
     * @return 채팅 정보 및 채팅 메시지 최대 30개를 조회한다.
     * **/
    @GetMapping("/chat/{meetingId}")
    public BaseResponse getChattingAll(@PathVariable Long meetingId, Authentication authentication) {

        // 사용자ID
        Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();

        // 해당 채팅방의 멤버가 맞는지 확인
        chattingService.isChattingMember(meetingId, memberId);

        // 채팅 관련 정보 가져오기
        ChattingResponseDto chattingResponseDto = chattingService.getChattingInfo(meetingId);

        // 응답값에 멤버 ID 넣기
        chattingResponseDto.setMemberId(memberId);

        return BaseResponse.success(chattingResponseDto);
    }

    /**
     * jeey0124
     * @param meetingId 미팅ID
     * @param lastNumber 마지막으로 조회했던 채팅 index
     * @return 채팅 메시지 최대 30개를 조회한다.
     * **/
    @GetMapping("/chat/{meetingId}/{lastNumber}")
    public BaseResponse getChattingLog(@PathVariable Long meetingId, @PathVariable Long lastNumber, Authentication authentication) {

        // 사용자ID
        Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();

        // 해당 채팅방의 멤버가 맞는지 확인
        chattingService.isChattingMember(meetingId, memberId);

        // 채팅 기록 전부 가져오기
        ChattingListDto chattingListDto = chattingService.getChattingLog(meetingId, lastNumber);

        return BaseResponse.success(chattingListDto);
    }
}
