package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetDateDto {

    private int days;
    private String userRole;
    private String matchingUserRole;
    private String userProfileImg;

    private String matchingUserProfileImg;
}
