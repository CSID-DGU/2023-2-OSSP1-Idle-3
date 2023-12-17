package com.almostThere.domain.map.controller;

import com.almostThere.domain.map.Service.LocationService;
import com.almostThere.domain.map.entity.UserLocation;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Qualifier("redisTemplateForLocation")
    private final RedisTemplate<String, UserLocation> redisTemplateForLocation;
    private final LocationService locationService;

    @Value("${redis.locationData.expire-length}")
    private long locationExpiretime;

    /**
     * 회원이 참요하고 있는 모임에 참여한 모든 회원들의 실시간 위치를 List에 담아 리턴한다.
     * @param meetingId
     * @param memberId
     * @param message
     * @return List<UserLocation>
     * @throws ParseException
     */
    @MessageMapping("/locShare/meetingId/{meetingId}/memberId/{memberId}")
    @SendTo("/topic/{memberId}")
    public List<UserLocation> getAllUsersLocations(@DestinationVariable long meetingId, @DestinationVariable long memberId, String message) throws ParseException {

        return locationService.getAllMemberLocationsByMeetingId(meetingId);
    }

    /**
     * 3시간 이내 모임이 존재하는 회원의 위치를 3초마다 REDIS에 저장한다.
     * 만약 이미 위치값이 존재하면 만료시간을 조회하여 해당 만료시간을 가지고 저장
     * 최초 저장이라면 만료시간을 3시간으로(현재는 60초로 지정) 저장
     * @param message
     * @throws ParseException
     */
    @MessageMapping("/locShare")
    public void saveLocation(String message) throws ParseException {

        System.out.println("메시지 정상 수신");
        JSONParser jsonParser = new JSONParser();
        JSONObject memberObject = (JSONObject) jsonParser.parse(message);

        String memberId = String.valueOf(memberObject.get("memberId"));
        String memberNickname = (String) memberObject.get("memberNickname");
        JSONArray memberLatLngJson = (JSONArray) memberObject.get("memberLatLng");
        double lat = (double) memberLatLngJson.get(0);
        double lng = (double) memberLatLngJson.get(1);

        UserLocation findUserLocation = (UserLocation) redisTemplateForLocation.opsForValue()
            .get(memberId);
        UserLocation userLocation = new UserLocation(Long.parseLong(memberId), memberNickname,
            new double[]{lat, lng});
    
        long expiretime = findUserLocation != null ? redisTemplateForLocation.getExpire(memberId): locationExpiretime;

        redisTemplateForLocation.opsForValue()
            .set(memberId, userLocation, expiretime, TimeUnit.SECONDS);

    }
}
