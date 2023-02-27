package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3ImageDto {
    private String imageUrl;
    private Long userId;
}
