package com.ch.jwtserver.member.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();     // Bcrypt 암호화가 한 번되면 역으로 돌릴 수 없다. 단반향
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 스프링 부트에서 설정해놓은 필터체인을 내가 원하는 방식으로 설정을 바꿔서, 바뀐 SecurityFilterChain 반환
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/test").permitAll()
                .anyRequest().authenticated()
        );

        return httpSecurity.build();
    }

}
