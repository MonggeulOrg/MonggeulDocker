package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetDateDto {

    private int days;
}
