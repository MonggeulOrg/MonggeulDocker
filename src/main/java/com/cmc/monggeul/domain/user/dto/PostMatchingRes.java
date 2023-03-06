package com.cmc.monggeul.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostMatchingRes {

    private String matchingUserName;
    private Long familyId;

    private String userProfileImg;

    private String matchingUserProfileImg;
}
