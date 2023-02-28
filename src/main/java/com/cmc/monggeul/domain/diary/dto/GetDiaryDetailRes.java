package com.cmc.monggeul.domain.diary.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetDiaryDetailRes {
    private String categoryName;
    private String questionName;

    private String childDiaryText;

    private String parentDiaryText;

    private String childEmotion;

    private String parentEmotion;


}
