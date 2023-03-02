package com.cmc.monggeul.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserInfoByMatchingCodeRes {

    private String name;
    private String email;

}
