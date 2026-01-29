package com.ch.jwtserver.member.service;

import com.ch.jwtserver.member.dto.MemberRequest;
import com.ch.jwtserver.member.entity.Member;
import com.ch.jwtserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 가입시키기
    @Transactional
    public Member regist(MemberRequest memberRequest) {

        Member member = new Member();
        member.setHomepageId(memberRequest.getHomepageId());
        member.setPassword(passwordEncoder.encode(memberRequest.getPassword()));
        member.setName(memberRequest.getName());

        return memberRepository.save(member);
    }

}
