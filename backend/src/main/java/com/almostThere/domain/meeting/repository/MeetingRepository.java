package com.almostThere.domain.meeting.repository;


import com.almostThere.domain.meeting.entity.Meeting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//내장 CRUD 이용시 이 인터페이스를 통해 호출
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    @Query("select m.meeting " +
            "from MeetingMember m " +
            "where m.member.id=:memberId " +
            "and m.meeting.meetingTime between now() and :oneDayAfterDate "+
            "order by m.meeting.meetingTime")
    List <Meeting> findTodayMeetings(@Param("memberId") Long memberId, @Param("oneDayAfterDate") LocalDateTime oneDayAfterDate);

    @Query("select m.meeting " +
            " from MeetingMember m " +
            "where m.member.id=:memberId " +
            "  and m.meeting.meetingTime between :oneDayAfterDate and :oneMonthAfterDate" +
            " order by m.meeting.meetingTime")
    List <Meeting> findUpcomingMeetings(@Param("memberId") Long memberId, @Param("oneDayAfterDate") LocalDateTime oneDayAfterDate, @Param("oneMonthAfterDate") LocalDateTime oneMonthAfterDate);

    List<Meeting> findAllByMeetingTime(LocalDateTime meetingTime);

    @Query("select m.meeting "
        + "from MeetingMember m "
        + "where m.member.id=:memberId "
        + "and m.meeting.meetingTime >= now()"
        + "order by m.meeting.meetingTime ")
    List<Meeting> getMostRecentMeeting(@Param("memberId") Long memberId, Pageable pageable);

    Optional<Meeting> findByRoomCode(@Param("roomCode") String roomCode);

    @Query("select m.meeting "
            + "from MeetingMember m "
            + "where m.member.id= :memberId "
            + "and m.meeting.meetingTime <= now()"
            + "order by m.meeting.meetingTime desc")
    List<Meeting> getRecentPastMeeting(@Param("memberId") Long memberId, Pageable pageable);
}
