package com.cmc.monggeul.domain.user.dto;

import lombok.Data;

@Data
public class PostAppleLoginReq {

    private String appleEmail;
    private String userName;

    private String role;

    private int age;

    private String profileImgUrl;
}
