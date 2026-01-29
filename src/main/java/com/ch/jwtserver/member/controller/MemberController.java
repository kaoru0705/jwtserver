package com.ch.jwtserver.member.controller;

import com.ch.jwtserver.member.dto.MemberRequest;
import com.ch.jwtserver.member.dto.MemberResponse;
import com.ch.jwtserver.member.entity.Member;
import com.ch.jwtserver.member.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class MemberController {

    private final AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public String getMsg() {
        return "hi nice to meet you!";
    }

    // 연습용 회원의 암호 생성 요청을 처리
    @PostMapping
    public Object regist(@RequestBody MemberRequest memberRequest) {
        // 서비스 객체 .regist()
        Member member = authService.regist(memberRequest);

        // 응답에 필요한 데이터 구성하기
        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setHomepageId(member.getHomepageId());
        memberResponse.setName(member.getName());

        return memberResponse;
    }
}
