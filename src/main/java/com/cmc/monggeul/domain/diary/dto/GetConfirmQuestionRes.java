package com.cmc.monggeul.domain.diary.dto;

import com.cmc.monggeul.domain.diary.entity.Diary;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetConfirmQuestionRes {

    private String categoryName;

    private String questionName;

    private Diary.DiaryStatus childStatus;

    private Diary.DiaryStatus parentStatus;

    private LocalDateTime createdAt;
}
