package com.cmc.monggeul.domain.user.dto;

import com.cmc.monggeul.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLoginReq {
    private String email;
    private String accessToken;

    private User.OAuthType oAuthType;
}
