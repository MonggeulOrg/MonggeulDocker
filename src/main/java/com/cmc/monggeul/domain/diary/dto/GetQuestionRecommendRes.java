package com.cmc.monggeul.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class GetQuestionRecommendRes {

    private Long questionId;
    private String questionName;

}
