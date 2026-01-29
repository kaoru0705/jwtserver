package com.ch.jwtserver.member.controller;

import com.ch.jwtserver.member.dto.LoginResponse;
import com.ch.jwtserver.member.dto.MemberRequest;
import com.ch.jwtserver.member.dto.MemberResponse;
import com.ch.jwtserver.member.entity.Member;
import com.ch.jwtserver.member.jwt.JwtTokenProvider;
import com.ch.jwtserver.member.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class MemberController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/test")
    public String getMsg() {
        return "hi nice to meet you!";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody MemberRequest memberRequest) throws IllegalAccessException{
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

        // 로그인에 성공한 유저에게는 응답 정보에 JWT 토큰을 적재하여 보내주자
        //jwtTokenProvider.createAccessToken(회원 아이디, 권한);
        String token = jwtTokenProvider.createAccessToken(member.getHomepageId(), List.of("ROLE_USER"));

        log.debug("token is {}", token);

        // Bearer Http 통신 시 헤더에 넣을 수 있는 공식적인 헤더값(인증 토큰 전송 시 그 형식을 의미)
        return new LoginResponse(token, "Bearer");
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
