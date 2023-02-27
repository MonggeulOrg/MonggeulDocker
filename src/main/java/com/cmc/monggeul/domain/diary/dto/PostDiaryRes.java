package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDiaryRes {

    private String categoryName;
    private String questionName;
    private Long diaryId;

    private String childDiaryText;

    private String parentDiaryText;

    private String childEmotion;

    private String parentEmotion;

    private String childImgUrl;

    private String parentImgUrl;
}
