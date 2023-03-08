package com.cmc.monggeul.domain.user.dto;


import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserMyPageDto {

    private String nickname;
    private int age;
    private String email;
    private String userRole;

    private User.OAuthType authType;
    private String userProfileImg;
    private int diaryCount;
    private int communityPostCount;

    private BaseEntity.Status status;



}
