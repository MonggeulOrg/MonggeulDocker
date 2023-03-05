package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
public class UserQuestionMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1(User) : N(Question)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="userId")
    private User user;

    // 1 (Question) : N(User)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="questionId")
    private Question question;


    // 답변 질문인지
    @Enumerated(value = EnumType.STRING)
    private QuestionStatus answerStatus;

    public UserQuestionMapping() {

    }


    public enum QuestionStatus{
        YES,
        NO
    }




}
