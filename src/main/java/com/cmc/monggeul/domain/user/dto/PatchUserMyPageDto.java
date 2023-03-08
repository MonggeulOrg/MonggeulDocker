package com.cmc.monggeul.domain.user.dto;

import com.cmc.monggeul.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatchUserMyPageDto {

    private String profileImg;
    private String name;
    private int age;

}
