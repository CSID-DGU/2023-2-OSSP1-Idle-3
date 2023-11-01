package com.almostThere.domain.meeting.service;

import com.almostThere.domain.chatting.dto.ChattingDto;
import com.almostThere.domain.chatting.repository.ChattingRepository;
import com.almostThere.domain.meeting.dto.AttendMeetingMemberDto;
import com.almostThere.domain.meeting.dto.MeetingDto;
import com.almostThere.domain.meeting.dto.MeetingTimeDto;
import com.almostThere.domain.meeting.dto.create.MeetingCreateRequestDto;
import com.almostThere.domain.meeting.dto.delete.MeetingDeleteRequestDto;
import com.almostThere.domain.meeting.dto.detail.MeetingCalculateDetailDto;
import com.almostThere.domain.meeting.dto.detail.MeetingDetailResponseDto;
import com.almostThere.domain.meeting.dto.detail.MeetingMemberResponseDto;
import com.almostThere.domain.meeting.dto.update.MeetingStartPlaceRequestDto;
import com.almostThere.domain.meeting.dto.update.MeetingUpdateRequestDto;
import com.almostThere.domain.meeting.entity.Meeting;
import com.almostThere.domain.meeting.entity.MeetingCnt;
import com.almostThere.domain.meeting.entity.MeetingMember;
import com.almostThere.domain.meeting.entity.StateType;
import com.almostThere.domain.meeting.repository.CalculateDetailRepository;
import com.almostThere.domain.meeting.repository.MeetingCntRepository;
import com.almostThere.domain.meeting.repository.MeetingMemberRepository;
import com.almostThere.domain.meeting.repository.MeetingRepository;
import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.AccessDeniedException;
import com.almostThere.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingService {
    private static final Logger logger = LoggerFactory.getLogger(MeetingService.class);
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final MeetingCntRepository meetingCntRepository;
    private final CalculateDetailRepository calculateDetailRepository;
    private final CalculateDetailService calculateDetailService;
    private final ChattingRepository chattingRepository;
    private final RedisTemplate<String, ChattingDto> redisTemplateForChatting;

    /**
     * 유저가 roomCode에 해당하는 모임에 가입해있는지 조회한다.
     * @param roomCode
     * @param memberId
     * @return 해당 모임의 meetingId를 반환한다.
     *          만약 가입되어 있지 않고 모임의 가입 가능 인원(10명)이 남았다면 가입시키고 meetingId를 반환하고,
     *          가입할 수 없다면 -1을 반환한다.
     */
    @Transactional
    public Long checkAndSaveMeetingMember(String roomCode, Long memberId){
        //가입할 모임 정보
        Meeting meeting = meetingRepository.findByRoomCode(roomCode)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEETING_NOT_FOUND));
        //모임 멤버 조회
        List<MeetingMember> meetingMembers = meeting.getMeetingMembers();
        
        //나의 멤버 인스턴스 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        //이미 입장한 멤버인지 조회
        Optional<MeetingMember> meetingMember = meetingMemberRepository.findByMeeting_IdAndMember_Id(meeting.getId(), member.getId());

        if(meetingMember.isPresent()){
            return meeting.getId();
        }

        if(meetingMembers.size()>=10){
            return -1L;
        }

        /**
         * 친구 만나는 횟수 저장(meeting_cnt)
         */
        //내가 만난 친구들 조회
        List<MeetingCnt> myFriends = meetingCntRepository.findByMyMember(member);

        //모임 멤버를 한 명씩 조회하면서 만남 횟수를 업데이트한다.
        for(MeetingMember m:meetingMembers){
            Member friend = m.getMember();
            Optional<MeetingCnt> present = myFriends.stream().filter(f -> f.getFriend().getId() == friend.getId()).findAny();

            //친구와 만난 적이 있으면 cnt+1
            if(present.isPresent()){
                //나를 기준으로 업데이트
                present.get().updateCnt();
                meetingCntRepository.save(present.get());
                //상대방 기준 조회 후 업데이트
                MeetingCnt friendCnt = meetingCntRepository.findByMyMemberAndFriend(friend, member);
                if(friendCnt!=null) {
                    friendCnt.updateCnt();
                    meetingCntRepository.save(friendCnt);
                }
                else{
                    MeetingCnt friendMeetingCnt = new MeetingCnt(friend,member);
                    meetingCntRepository.save(friendMeetingCnt);
                }
            }
            else{   //만난적이 없으면 save
                //내 기준으로 insert
                MeetingCnt myMeetingCnt = new MeetingCnt(member,friend);
                meetingCntRepository.save(myMeetingCnt);
                //상대방 기준으로 insert
                MeetingCnt friendMeetingCnt = new MeetingCnt(friend,member);
                meetingCntRepository.save(friendMeetingCnt);
            }
        }

        //방에 입장
        Long meetingId = meeting.getId();
        MeetingMember m = new MeetingMember(member, meeting, StateType.GOING);
        meeting.getMeetingMembers().add(m);
        meetingRepository.save(meeting);
        calculateDetailService.updateSpentMoney(meeting);
        return meetingId;
    }
    /**
     * 지금 시간 이후로 가장 빠른 미팅 조회
     * @param memberId
     * @return
     */
    public MeetingTimeDto getMostRecentMeeting(Long memberId){

        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Meeting> meeting = meetingRepository.getMostRecentMeeting(memberId, pageRequest);
        if(meeting.size()==1) {
            MeetingTimeDto meetingTimeDto = new MeetingTimeDto(meeting.get(0));
            System.out.println("가장 최근 모임의 시간 :"+meetingTimeDto.getMeetingTime());
            return meetingTimeDto;
        }

        return null;
    }

    /**
     * 멤버가 참여한 오늘 예정인 모임을 조회한다.
     * @param memberId
     * @return 모임 리스트
     */
    public List<MeetingDto> findTodayMeeting(Long memberId){
        LocalDateTime afterDate = LocalDateTime.now().plusDays(1);
        List<Meeting> meetings = meetingRepository.findTodayMeetings(memberId,afterDate);
        List<MeetingDto> result = meetings.stream().map(m->new MeetingDto(m)).collect(Collectors.toList());
        return result;
    }

    /**
     * 멤버가 참여한 한 달 이내의 모임을 조회한다.
     * @param memberId
     * @return 모임 리스트
     */
    public List<MeetingDto> findUpcomingMeeting(Long memberId){
        LocalDateTime oneMonthAfterDate = LocalDateTime.now().plusMonths(1);
        LocalDateTime oneDayAfterDate = LocalDateTime.now().plusDays(1);
        System.out.println(oneDayAfterDate);
        System.out.println(oneMonthAfterDate);
        List<Meeting> meetings = meetingRepository.findUpcomingMeetings(memberId,oneDayAfterDate,oneMonthAfterDate);
        List<MeetingDto> result = meetings.stream().map(m->new MeetingDto(m)).collect(Collectors.toList());
        return result;
    }

    /**
     * 모임을 만드는 사람을 host로 모임을 생성한다.
     * @param meetingCreateRequestDto
     * @return 없음
     */
    @Transactional
    public Long createMeeting(MeetingCreateRequestDto meetingCreateRequestDto){
        final Member meetingHost = memberRepository.findById(meetingCreateRequestDto.getHostId())
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEMBER_NOT_FOUND));

        Random rand = new Random();
        int roomCode = (int)(rand.nextLong()%100000000L);
        roomCode = Math.abs(roomCode);
        String rc = Integer.toString(roomCode);
//        meetingCreateRequestDto.setMeetingTime(meetingCreateRequestDto.getMeetingTime().plusHours(9));
        meetingCreateRequestDto.setMeetingTime(meetingCreateRequestDto.getMeetingTime());
        Meeting meeting = meetingCreateRequestDto.toEntity(meetingCreateRequestDto, meetingHost,rc);
        meeting = meetingRepository.save(meeting);
        MeetingMember meetingMember = new MeetingMember(meetingHost, meeting, StateType.GOING);
        meetingMemberRepository.save(meetingMember);

        return meeting.getId();
    }

    /**
     * 모임상세정보, 모임멤버, 정산상세내역을 조회한다.
     * @param
     * @return 모임 상세정보
     */
    public MeetingDetailResponseDto getMeetingDetail(Long memberId, Long meetingId){
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new NotFoundException(
                        ErrorCode.MEETING_NOT_FOUND));
        MeetingMember meetingMember = meetingMemberRepository.findByMeeting_IdAndMember_Id(
                meetingId,
                memberId
        ).orElseThrow(() -> new NotFoundException(ErrorCode.MEETING_MEMBER_NOT_FOUND));

        List<MeetingMemberResponseDto> meetingMembers = meeting.getMeetingMembers()
                .stream().map(m->new MeetingMemberResponseDto(m)).collect(Collectors.toList());
        MeetingMember member = meeting.getMeetingMembers().stream().filter(m->m.getMember().getId()==memberId).findAny().get();

        List<MeetingCalculateDetailDto> calculateDetails = new ArrayList<>();
        if(meeting.getCalculateDetails() != null) {
            calculateDetails = meeting.getCalculateDetails()
                    .stream().map(c->new MeetingCalculateDetailDto(c)).collect(Collectors.toList());
        }
        int totalPrice = calculateDetailRepository.sumMeetingPrice(meeting.getId());
        int memberPrice = meetingMemberRepository.sumMemberPrice(meeting.getId());
        int remain = totalPrice-memberPrice;
        return new MeetingDetailResponseDto(meeting, remain, meetingMembers, calculateDetails);
    }

    /**
     * Host가 모임을 삭제한다.
     * @param
     */
    @Transactional
    public void deleteMeeting(Long meetingId) {
        final Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEETING_NOT_FOUND));
        meetingRepository.deleteById(meetingId);
    }

    /**
     * 모임에서 나간다.
     * @param meetingDeleteRequestDto
     */
    @Transactional
    public void exitMeeting(MeetingDeleteRequestDto meetingDeleteRequestDto){
        meetingMemberRepository.deleteMeetingMemberByMeetingIdAndMemberID(
                meetingDeleteRequestDto.getMemberId(),meetingDeleteRequestDto.getMeetingId());

        // 해당 멤버의 채팅도 삭제 1. MySQL
        chattingRepository.deleteByMember_Id(meetingDeleteRequestDto.getMemberId());
        // 해당 멤버의 채팅도 삭제 2. Redis
        String key = "chat:" + meetingDeleteRequestDto.getMeetingId();
        ListOperations<String, ChattingDto> listOperations = redisTemplateForChatting.opsForList();
        List<ChattingDto> chattingDtoList = listOperations.range(key, 0, listOperations.size(key));
        for (ChattingDto chattingDto : chattingDtoList) {
            if (chattingDto.getMemberId().equals(meetingDeleteRequestDto.getMemberId())) {
                listOperations.remove(key, 1, chattingDto);
            }
        }
    }

    /**
     * 모임 상세정보를 수정한다.
     * @param meetingUpdateRequestDto
     */
    @Transactional
    public void updateMeeting(MeetingUpdateRequestDto meetingUpdateRequestDto){
        final Meeting meeting = meetingRepository.findById(meetingUpdateRequestDto.getMeetingId())
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEETING_NOT_FOUND));

        if (meeting.getHost().getId() == meetingUpdateRequestDto.getHostId()) {
            meeting.updateMeeting(meetingUpdateRequestDto);
            meetingRepository.save(meeting);
        }   else throw new AccessDeniedException(ErrorCode.NOT_AUTHORIZATION);
    }

    /**
     * 멤버가 참여한 모든 모임을 조회한다.
     * @param memberId
     * @return 모임 리스트
     */
    public List<AttendMeetingMemberDto> findAttendAllMeetingById(Long memberId) {
        // member가 참여한 모임멤버(+ 모임) List 조회
        return meetingMemberRepository.findByMemberId(memberId).stream().map(m -> new AttendMeetingMemberDto(m)).collect(Collectors.toList());
    }

    /**
     * 모임ID + 멤버ID에 따른 출발장소를 수정한다.
     * @param
     */
    @Transactional
    public void updateMemberStartPlace(MeetingStartPlaceRequestDto meetingStartPlaceRequestDto){
        // i) 로그인 한 사용자의 meeting-member 조회
        MeetingMember meetingMember = meetingMemberRepository.findByMeeting_IdAndMember_Id(
                meetingStartPlaceRequestDto.getMeetingId(),
                meetingStartPlaceRequestDto.getMemberId()
        ).orElseThrow(() -> new NotFoundException(ErrorCode.MEETING_MEMBER_NOT_FOUND));

        // ii) 변경된 출발장소 set
        meetingMember.updateStartPlace(meetingStartPlaceRequestDto);
        // iii) meeting-member 저장
        meetingMemberRepository.save(meetingMember);
    }

    /**
     * 현재 시각을 기준으로 최근에 지난 모임시간을 조회한다.
     * @param
     */
    public MeetingTimeDto getRecentPastMeeting(Long memberId){
        List<Meeting> meeting = meetingRepository.getRecentPastMeeting(memberId, PageRequest.of(0, 1));
        if (meeting.size() != 0) {
            return new MeetingTimeDto(meeting.get(0));
        }

        return null;
    }
}
