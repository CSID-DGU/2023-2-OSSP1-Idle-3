package com.almostThere.domain.meeting.controller;

import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.dto.MeetingTimeDto;
import com.almostThere.domain.meeting.dto.create.MeetingCreateRequestDto;
import com.almostThere.domain.meeting.dto.delete.MeetingDeleteRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingDetailResponseDto;
import com.almostThere.domain.meeting.dto.update.MeetingStartPlaceRequestDto;
import com.almostThere.domain.meeting.dto.update.MeetingUpdateRequestDto;
import com.almostThere.domain.meeting.service.MeetingService;
import com.almostThere.domain.user.dto.MemberAccessDto;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.NotFoundException;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/meeting")
public class MeetingApiController {

    private final MeetingService meetingService;

    /**
     * 사용자가 름코드에 해당하는 모임에 가입되어 있는지 확인 후 가입시킨다.
     * 모임멤버들의 친구만남 횟수를 업데이트한다.
     * @param authentication
     * @param roomCode
     * @return
     */
    @GetMapping("/meeting-member/{roomCode}")
    public BaseResponse checkAndSaveMeetingMember(Authentication authentication, @PathVariable String roomCode){

        System.out.println("roomCode :"+ roomCode);
        Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();
        Long meetingId = meetingService.checkAndSaveMeetingMember(roomCode, memberId);

        return meetingId != -1L ? BaseResponse.customSuccess(200, "가입 or 멤버 확인이 성공적으로 완료됨", meetingId)
            : BaseResponse.customSuccess(403, "가입 정원 초과", meetingId);
    }

    /**
     * 소켓 연결 시간 설정을 위해 다가올 모임 중 가장 이른 모임을 조회한다.
     * @param authentication for memberId
     * @return
     */
    @GetMapping("/most-recent")
    public BaseResponse getMostRecentMeeting(Authentication authentication){

        Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();
        MeetingTimeDto meetingTimeDto = meetingService.getMostRecentMeeting(memberId);

        return BaseResponse.success(meetingTimeDto);

    }
    /**
     * dyeon7310
     * @param
     * @return 오늘의 모임(24시간 이내)을 조회한다.
     */
    @GetMapping("/today")
    public BaseResponse getTodayMeetings(Authentication authentication){
        Long memberId = ((MemberAccessDto)authentication.getPrincipal()).getId();
        List<MeetingDto> todayMeeting = meetingService.findTodayMeeting(memberId);
        return BaseResponse.success(todayMeeting);
    }

    /**
     * dyeon7310
     * @param
     * @return 앞으로 한 달 이내의 모임을 조회한다.
     */
    @GetMapping("/upcoming")
    public BaseResponse getUpcomingMeetings(Authentication authentication){
        Long memberId = ((MemberAccessDto)authentication.getPrincipal()).getId();
        List<MeetingDto> upcomingMeeting = meetingService.findUpcomingMeeting(memberId);
        return BaseResponse.success(upcomingMeeting);
    }

    /**
     * asng
     * @param meetingCreateRequestDto
     * @return 모임을 생성한다.
     */
    @PostMapping
    public BaseResponse createMeeting(@RequestBody MeetingCreateRequestDto meetingCreateRequestDto){
        Long meetingId = meetingService.createMeeting(meetingCreateRequestDto);
        return new BaseResponse(200, "SUCCESS", meetingId);
    }

    /**
     * asng
     * @param
     * @return 모임 상세정보를 조회한다.
     */
    @GetMapping("/detail/{meetingId}")
    public BaseResponse getMeetingDetail(Authentication authentication, @PathVariable Long meetingId) {
        MeetingDetailResponseDto meetingDetailResponseDto = null;
        try {
            Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();
            meetingDetailResponseDto = meetingService.getMeetingDetail(memberId, meetingId);
        }catch (NotFoundException e) {
            if(e.getErrorCode().equals(ErrorCode.MEETING_NOT_FOUND)){
                System.out.println("존재하지 않는 미팅입니다.");
                return BaseResponse.customFail(ErrorCode.MEETING_NOT_FOUND);
            }
            else if(e.getErrorCode().equals(ErrorCode.MEETING_MEMBER_NOT_FOUND)){
                System.out.println("존재하지 않는 멤버입니다");
                return BaseResponse.customFail(ErrorCode.MEETING_MEMBER_NOT_FOUND);
            }
        }
        return BaseResponse.success(meetingDetailResponseDto);
    }

    /**
     * asng
     * @param
     * @return 모임을 삭제한다.
     */
    @DeleteMapping("/{meetingId}")
    public BaseResponse deleteMeeting(@PathVariable Long meetingId){
        meetingService.deleteMeeting(meetingId);
        return new BaseResponse(200, "SUCCESS",null);
    }

    /**
     * asng
     * @param meetingDeleteRequestDto
     * @return 모임방에서 나간다.
     */
    @PutMapping("/exit")
    public BaseResponse exitMeeting(@RequestBody MeetingDeleteRequestDto meetingDeleteRequestDto){
        meetingService.exitMeeting(meetingDeleteRequestDto);
        return new BaseResponse(200, "SUCCESS",null);
    }

    /**
     * asng
     * @param meetingUpdateRequestDto
     * @return 모임 상세정보를 수정한다.
     */
    @PutMapping
    public BaseResponse updateMeeting(@RequestBody MeetingUpdateRequestDto meetingUpdateRequestDto){
        meetingService.updateMeeting(meetingUpdateRequestDto);
        return new BaseResponse(200, "SUCCESS",null);
    }

    /**
     * halo
     * 모임ID + 멤버ID에 따른 출발장소를 수정한다.
     * @param meetingStartPlaceRequestDto
     * @return
     */
    @PostMapping("/start-place")
    public BaseResponse setStartPlace(@RequestBody MeetingStartPlaceRequestDto meetingStartPlaceRequestDto, Authentication authentication) {
//        System.out.println("#[MemberController]# setStartPlace 출발장소 수정 - request: " + meetingStartPlaceRequestDto);

        // * token을 활용하여 현 로그인 member의 id 추출
        meetingStartPlaceRequestDto.setMemberId(((MemberAccessDto)authentication.getPrincipal()).getId());

        // 변경된 출발장소 update
        meetingService.updateMemberStartPlace(meetingStartPlaceRequestDto);

        return BaseResponse.success(null);
    }

    /**
     * halo
     * 현재 시각을 기준으로 최근에 지난 모임시간을 조회한다.
     * @param
     * @return meetingTimeDto
     */
    @GetMapping("/past-recent")
    public BaseResponse getRecentPastMeetingTime(Authentication authentication){
//        System.out.println("#[MemberController]# getRecentPastMeetingTime 최근에 지난 모임시간 조회");
        MeetingTimeDto meetingTimeDto = meetingService.getRecentPastMeeting(((MemberAccessDto) authentication.getPrincipal()).getId());

        if (meetingTimeDto == null) return BaseResponse.fail();
        return BaseResponse.success(meetingTimeDto);
    }
}
