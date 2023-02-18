package com.cmc.monggeul.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoUserDto {

    private String email;
    private String profileImgUrl;

}
