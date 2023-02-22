package com.cmc.monggeul.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostGoogleLoginReq {

    private String googleAccessToken;

    private String userName;

    private String role;

    private int age;
}
