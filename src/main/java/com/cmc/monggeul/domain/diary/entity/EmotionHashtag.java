package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class EmotionHashtag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private String hashtagImgUrl;

    @OneToMany(mappedBy = "emotionHashtag")
    private List<Diary> diaryList=new ArrayList<>();

}
