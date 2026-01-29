package com.ch.jwtserver.member.repository;

import com.ch.jwtserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 홈페이지 id를 이용한 정보가져오는 메서드는 기본적으로 지원하지 않기 때문에 개발자가 직접 정의해야 함
    Optional<Member> findByHomepageId(String homepageId);
}
