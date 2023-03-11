package com.cmc.monggeul.domain.user.dto;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetFamilyDto {

    private Long familyId;
    private Long childId;
    private String childName;

    private String childImg;
    private Long parentId;
    private String parentName;

    private String parentImg;

    private BaseEntity.Status status;
}
