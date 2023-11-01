package com.almostThere.domain.map.Service;

import com.almostThere.domain.chatting.dto.ChattingDto;
import com.almostThere.domain.chatting.entity.Chatting;
import com.almostThere.domain.chatting.repository.ChattingRepository;
import com.almostThere.domain.map.dto.MapResponseDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.meeting.repository.MeetingMemberRepository;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingRepository meetingRepository;
    private final ChattingRepository chattingRepository;
    private final RedisTemplate<String, ChattingDto> redisTemplateForChatting;
    private final double earthRadius = 6400;

    /**
     * @param  meetingId 모임ID
     * @param memberId 멤버ID
     * @return memberId에 해당하는 MeetingMember Entity
     * **/
    public MeetingMember isMeetingMember(Long meetingId, Long memberId) {

        // memberId가 meetingMember에 속해 있는지 확인
        Optional<MeetingMember> meetingMemberOptional = meetingMemberRepository.findByMeeting_IdAndMember_Id(meetingId, memberId);
        if (meetingMemberOptional.isPresent()) return meetingMemberOptional.get();
        else throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZATION);
    }

    /**
     * @param  meetingId 모임ID
     * @return meetingId에 해당하는 Meeting
     * **/
    public Meeting isMeeting(Long meetingId) {
        Optional<Meeting> meetingOptional = meetingRepository.findById(meetingId);
        if (meetingOptional.isPresent()) return meetingOptional.get();
        else throw new AccessDeniedException(ErrorCode.MEETING_NOT_FOUND);
    }

    /**
     * @param  meetingTime 모임 시간
     * @return 모임 시간이 현재 시간 이후인지 판단
     * **/
    public void isMeetingTimeAfterNow(LocalDateTime meetingTime) {
        boolean result = meetingTime.isAfter(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        if (!result) throw new AccessDeniedException(ErrorCode.TIMEOVER);
    }

    /**
     * @param x1 첫 번째 좌표의 위도
     * @param y1 첫 번째 좌표의 경도
     * @param x2 두 번째 좌표의 위도
     * @param y2 두 번째 좌표의 경도
     * @return 두 좌표 사이의 거리 (단위 m)
     * **/
    public double calculateDistance(double x1, double y1, double x2, double y2) {

        // x 위도(latitude), y 경도(longitude) 간의 거리 구하기
        double dLat = Math.toRadians(x2 - x1);
        double dLon = Math.toRadians(y2 - y1);
        double a = Math.sin(dLat/2)* Math.sin(dLat/2)+ Math.cos(Math.toRadians(x1))* Math.cos(Math.toRadians(x2))* Math.sin(dLon/2)* Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c * 1000;
        return distance;
    }

    /**
     * @param meetingMember 업데이트할 meetingMember Entity
     * @param stateType 업데이트 후의 값
     * **/
    public void updateMemberState(MeetingMember meetingMember, StateType stateType) {
        meetingMember.updateState(stateType);
        meetingMemberRepository.save(meetingMember);
    }

    /**
     * 현재 시간 기준 1분 전이 모임 시간이었던 경우 GOING을 전부 LATE로 변경
     * **/
    @Scheduled(cron = "0 * * * * *") // 테스트 위해 1분 주기
    @Transactional
    public void changeState() {

        // 현재 시간 1분 전
        LocalDateTime nowBefore1Minute = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusMinutes(1).withNano(0);
        
        // 현재 시간 1분 전에 끝난 meeting 가져오기
        List<Meeting> meetingList = meetingRepository.findAllByMeetingTime(nowBefore1Minute);
        for (Meeting meeting : meetingList) {
            Long meetingId = meeting.getId();
            meetingMemberRepository.updateMeetingMemberState(meetingId);
        }
    }

    /**
     * LiveMap에서 필요한 정보를 조회한다
     * **/
    public MapResponseDto getLiveMapInfo(Long meetingId) {

        Meeting meeting = isMeeting(meetingId);

        LocalDateTime meetingTime = meeting.getMeetingTime();
        LocalDateTime startTime = meetingTime.minusHours(3);
        LocalDateTime endTime = meetingTime.plusHours(3);

        // MySQL에서 채팅 정보를 가져온다.
        List<Chatting> chattingList = chattingRepository.findRecentMessage(meetingId, startTime, endTime);
        MapResponseDto mapResponseDto = new MapResponseDto(meeting, chattingList);

        // redis에서 meetingId의 채팅 정보를 가져온다.
        ListOperations<String, ChattingDto> listOperations = redisTemplateForChatting.opsForList();
        List<ChattingDto> chattingDtoList = listOperations.range("chat:"+meetingId, 0, listOperations.size("chat:"+meetingId)).stream()
                                                            .sorted(Comparator.comparing(ChattingDto::getChattingTime)).collect(Collectors.toList());
        Map<Long, String> chattingDtoMap = mapResponseDto.getChattingDtoMap();
        for (ChattingDto chattingDto : chattingDtoList) {
            Long memberId = chattingDto.getMemberId();
            if (chattingDtoMap.containsKey(memberId)) {
                // 이미 배열에 존재한다면 값 변경
                mapResponseDto.getChattingDtoMap().replace(memberId, chattingDto.getMessage());
            } else {
                // MySQL에서 가져온 값이 없고 Redis에는 있다면 Redis 값 저장
                mapResponseDto.getChattingDtoMap().put(memberId, chattingDto.getMessage());
            }
        }

        return mapResponseDto;
    }
}