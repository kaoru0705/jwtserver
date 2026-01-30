package com.ch.jwtserver.member.jwt;

import io.jsonwebtoken.
        Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/*
    JWT 토큰을 발급하고, 해석을 담당하는 클래스
    jjwt api        0.12.6
    jjwt impl       0.12.6
    jjwt jackson    0.12.6
 */
@Component
public class JwtTokenProvider {

    private SecretKey signature;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret) {
        this.signature = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /*---------------------------------------------------------
    토큰 생성
    ---------------------------------------------------------*/
    public String createAccessToken(String user, List<String> roles) {
        Instant now = Instant.now();
        // 실무적으로 5분에서 10분
        Instant exp = now.plusSeconds(60 * 60);

        // new JWT(A, B, C, D)
        return Jwts.builder()
                .subject(user)    // sub 주제, 무엇과 관련된 것인가? 회원과 관련된 것임
                //.claim("key", "value")  // 공식 표준 데이터는 아니지만 개발자들이 주로 권한..(일반 유저, 관리자...)
                .claim("roles", "ROLE_USER")
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signature)     // 서명

                .compact();    //
    }

    /*---------------------------------------------------------
    토큰 해석 ( 토큰 안의 값 추출)
    ---------------------------------------------------------*/
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signature)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
