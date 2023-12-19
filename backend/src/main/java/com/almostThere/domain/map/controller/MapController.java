package com.almostThere.domain.map.controller;

import com.almostThere.domain.map.Service.MapService;
import com.almostThere.domain.map.dto.MapResponseDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.user.dto.MemberAccessDto;
import com.almostThere.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    /**
     * 서지윤
     * @param meetingId 모임ID
     * @param x 위도(latitude)
     * @param y 경도(longitude)
     * @return 도착 완료 시 success, 실패 시 fail
     * **/
    @PostMapping("/arrive/{meetingId}")
    public BaseResponse reach(@PathVariable Long meetingId, @RequestParam double x, @RequestParam double y, Authentication authentication) {

        Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();
        
        // memberId가 meetingMember에 속해 있는지 확인
        MeetingMember meetingMember = mapService.isMeetingMember(meetingId, memberId);

        // meetingId로 meeting 정보 가져오기
        Meeting meeting = mapService.isMeeting(meetingId);

        // 현재 시간보다 meetingTime이 이후인지 확인
        mapService.isMeetingTimeAfterNow(meeting.getMeetingTime());
        
        // 위도와 경도로 거리 구하기
        double distance = mapService.calculateDistance(x, y, meeting.getMeetingLat(), meeting.getMeetingLng());

        // 거리가 100m 이하
        if (distance <= 100) {
            mapService.updateMemberState(meetingMember, StateType.ARRIVE);
            return BaseResponse.success(null);
        }
        else return BaseResponse.fail();
    }

    /**
     * 서지윤
     * @param meetingId 모임ID
     * @return 멤버별 최근 채팅 1개씩 꺼내기
    **/
    @GetMapping("/{meetingId}")
    public BaseResponse recentChat(@PathVariable Long meetingId, Authentication authentication) {

        Long memberId = ((MemberAccessDto) authentication.getPrincipal()).getId();

        // memberId가 meetingMember에 속해 있는지 확인
        mapService.isMeetingMember(meetingId, memberId);

        // LiveMap에서 필요한 정보 불러오기
        MapResponseDto mapResponseDto = mapService.getLiveMapInfo(meetingId);
        mapResponseDto.setMemberId(memberId);

        return BaseResponse.success(mapResponseDto);
    }
}