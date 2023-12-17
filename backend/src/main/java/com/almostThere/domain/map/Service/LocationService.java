package com.almostThere.domain.map.Service;

import com.almostThere.domain.map.entity.UserLocation;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final MeetingRepository meetingRepository;
    @Qualifier("redisTemplateForLocation")
    private final RedisTemplate redisTemplateForLocation;

    /**
     * 모임의 모든 참가자 목록을 My-SQL에서 조회 후 redis에서 해당 참가자들의 실시간 위치를 조회한다.
     * @param meetingId
     * @return List<UserLocation>
     */
    public List<UserLocation> getAllMemberLocationsByMeetingId(long meetingId){
        ObjectMapper objectMapper = new ObjectMapper();
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEETING_NOT_FOUND));

        List<MeetingMember> meetingMembers = meeting.getMeetingMembers();
        List<UserLocation> userLocations = new ArrayList<>();

        for(MeetingMember meetingMember : meetingMembers){
            String memberId = String.valueOf(meetingMember.getMember().getId());
             UserLocation userLocation = (UserLocation)redisTemplateForLocation.opsForValue().get(memberId);
            userLocations.add(userLocation);
        }

        return userLocations;
    }

}
