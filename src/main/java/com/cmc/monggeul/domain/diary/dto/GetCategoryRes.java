package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetCategoryRes {

    private String categoryCode;
    private String categoryName;
    private String subName;

}
