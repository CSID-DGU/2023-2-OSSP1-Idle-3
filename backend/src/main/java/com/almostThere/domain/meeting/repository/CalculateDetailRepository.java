package com.almostThere.domain.meeting.repository;

import com.almostThere.domain.meeting.entity.CalculateDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalculateDetailRepository extends JpaRepository<CalculateDetail, Long> {

    @Query("select COALESCE(sum(c.price),0) from CalculateDetail c where c.meeting.id = :meetingId")
    int sumMeetingPrice(@Param("meetingId") Long meetingId);
}
