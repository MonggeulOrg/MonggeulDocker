package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class EmotionHashtag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private String hashtagImgUrl;

    @OneToMany(mappedBy = "childEmotionHashtag")
    private List<Diary> childDiaryList=new ArrayList<>();

    @OneToMany(mappedBy = "parentEmotionHashtag")
    private List<Diary> parentDiaryList=new ArrayList<>();

}
