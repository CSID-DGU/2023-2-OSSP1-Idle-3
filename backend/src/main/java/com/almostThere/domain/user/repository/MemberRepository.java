package com.almostThere.domain.user.repository;

import com.almostThere.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByMemberEmail(String email);
    Member findByid(Long memberId);
}
