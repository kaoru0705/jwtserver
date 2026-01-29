package com.ch.jwtserver.member.dto;

import lombok.Getter;
import lombok.Setter;

/*
* Member 엔티티를 응답정보나 요청 정보에 사용할 수도 있으나, 특히 응답 정보로 사용해버리면
* 너무나 민감한 정보가 json 문자열로 클라이언트에게 그대로 전송되어 버린다.. 심지어.. password까지..
* 따라서 Member entity는 요청, 응답에 직접 사용해서는 안되며, 반드시 요청과 응답에 필요한 필드만 따로 떼어서
* DTO로 정의해놓아야 한다.
* */
@Getter
@Setter
public class MemberResponse {

    private String homepageId;
    private String name;

}
