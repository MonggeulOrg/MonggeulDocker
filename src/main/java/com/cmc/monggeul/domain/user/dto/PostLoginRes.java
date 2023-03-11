package com.cmc.monggeul.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLoginRes {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    private String profileImg;

    //인증코드
    private String code;
}
