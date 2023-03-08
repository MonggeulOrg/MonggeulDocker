package com.cmc.monggeul.domain.user.dto;

import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserStatus {

    private String nickname;
    private String email;
    private BaseEntity.Status status;
}
