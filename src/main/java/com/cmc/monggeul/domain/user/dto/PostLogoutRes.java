package com.cmc.monggeul.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLogoutRes {

    private String userEmail;

}
