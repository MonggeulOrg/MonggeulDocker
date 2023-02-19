package com.cmc.monggeul.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostKakaoLoginRes {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    //인증코드
    private String code;
}
