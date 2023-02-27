package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostResponseDiaryReq {

    private Long diaryId;
    private Long familyId;
    private String text;
    private String imgUrl;

    private String emotionHashtag;
}
