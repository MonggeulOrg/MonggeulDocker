package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetDateDto {

    private int days;
    private String role;
    private String matchingUserRole;
    private String profileImg;

    private String matchingUserProfileImg;
}
