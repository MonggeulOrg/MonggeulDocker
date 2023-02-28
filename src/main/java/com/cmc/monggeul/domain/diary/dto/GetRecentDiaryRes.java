package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetRecentDiaryRes {

    private Long diaryId;
    private String categoryName;
    private String questionName;

    private String imgUrl;

}
