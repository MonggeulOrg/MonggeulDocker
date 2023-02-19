package com.cmc.monggeul.domain.user.dto;

import lombok.Data;

@Data
public class PostKakaoLoginReq {

    private String kakaoAccessToken;

    private String userName;

    private String role;

    private String age;
}
