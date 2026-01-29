package com.ch.jwtserver.member.controller;

import com.ch.jwtserver.member.dto.MemberRequest;
import com.ch.jwtserver.member.dto.MemberResponse;
import com.ch.jwtserver.member.entity.Member;
import com.ch.jwtserver.member.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @PostMapping("/login")
    public MemberResponse login(@RequestBody MemberRequest memberRequest) throws IllegalAccessException{
        // 원래는 스프링 시큐리티가 알아서 회원정보 검증을 수행하지만, 현재로는 formLogin 비활성화 시켜놓음
        // 참고로 formLogin.disable()했다고 해서, 시큐리티의 필터체인이 동작하지 않는 건 아님
        // formLogin.disable() 은 UsernamePasswordAuthenticationFilter를 통한 요청 흐름을 안 타겠다는 의미
        // 따라서 개발자는 필터체인 중 원하는 시점에 참여하는 코드를 작성하면 여전히 필터체인은 동작할 수 있다.
        // 예) AuthenticationManager의 호출을 개발자가 직접해버리면, 이후의 요청 처리가 동작될 수 있다.

        // 회원정보를 가져와서
        // 데이터베이스에서 가져온 Member에는 이미 암호화된 비밀번호가 들어있다.
        Member member = authService.findByHomepageId(memberRequest.getHomepageId());

        // 비밀번호 검증을 처리
        if(!authService.matchPassword(memberRequest.getPassword(), member)) {
            log.debug("비밀번호 불일치");
            throw new IllegalAccessException("로그인 정보가 올바르지 않습니다.");
        }

        log.debug("회원 인증 성공");
        
        // 비밀번호 검증을 처리..
        return null;
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
