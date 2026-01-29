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

    // 회원 한 명 가져오기
    @Transactional(readOnly = true)
    public Member findByHomepageId(String homepageId) {
        return memberRepository.findByHomepageId(homepageId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // PasswordEncoder를 이용하여, 그냥 날것인 RawPassword와 DB의 암호화된 비번을 비교해보기
    public boolean matchPassword(String rawPassword, Member member) {
        return passwordEncoder.matches(rawPassword, member.getPassword());
    }
}
