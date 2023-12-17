package com.almostThere.domain.chatting.repository;

import com.almostThere.domain.chatting.entity.Chatting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Long> {

    Long countByMeeting_Id(Long Id);

    Page<Chatting> findAllByMeeting_IdOrderByChattingTimeDescIdDesc(Long id, Pageable pageable);

//    @Query("SELECT pc.message, pc.member.id " +
//            "FROM (SELECT ch, row_number() OVER(PARTITION BY ch.member.id ORDER BY ch.chattingTime DESC) AS rn " +
//            "FROM Chatting ch WHERE ch.meeting.id = :meetingId AND ch.chattingTime BETWEEN :startTime AND :endTime ) AS pc " +
//            "WHERE pc.rn = 1")
    @Query("SELECT ch2 FROM Chatting ch2 WHERE (ch2.member.id, ch2.chattingTime)" +
            "IN (SELECT ch1.member.id, MAX(ch1.chattingTime) AS ct FROM Chatting ch1 WHERE ch1.meeting.id = :meetingId AND ch1.chattingTime BETWEEN :startTime AND :endTime GROUP BY ch1.member.id)")
    List<Chatting> findRecentMessage(@Param("meetingId") Long meetingId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    void deleteByMember_Id(Long meetingId);
}