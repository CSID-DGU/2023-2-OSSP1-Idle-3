package com.almostThere.domain.chatting.repository;

import com.almostThere.domain.chatting.dto.ChattingDto;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ChattingJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChattingJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(List<ChattingDto> chattingList, Long meetingId) {

        jdbcTemplate.batchUpdate(
            "INSERT INTO chatting (message, meeting_id, member_id, chatting_time)" +
                 "VALUES (?, ?, ?, ?)",

                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, chattingList.get(i).getMessage());
                        ps.setLong(2, meetingId);
                        ps.setLong(3, chattingList.get(i).getMemberId());
                        ps.setString(4, chattingList.get(i).getChattingTime().toString());
                    }
                    @Override
                    public int getBatchSize() {
                        return chattingList.size();
                    }
                }
        );
    }
}
