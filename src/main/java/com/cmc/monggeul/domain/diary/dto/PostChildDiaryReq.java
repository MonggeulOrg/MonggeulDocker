package com.cmc.monggeul.domain.diary.dto;

import lombok.Data;

@Data
public class PostChildDiaryReq {

    private Long familyId;

    private Long questionId;
    private String text;
    private String imgUrl;

    private String emotionHashtag;
}
