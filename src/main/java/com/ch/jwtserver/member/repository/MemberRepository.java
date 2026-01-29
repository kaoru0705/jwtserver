package com.ch.jwtserver.member.repository;

import com.ch.jwtserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
