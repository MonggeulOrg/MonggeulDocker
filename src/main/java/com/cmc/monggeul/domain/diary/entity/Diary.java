package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.entity.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1(question) : N(diary)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "questionId")
    private Question question;

    @Lob
    private String parentText;
    @Lob
    private String parentImageURL;

    @Lob
    private String childText;

    @Lob
    private  String childImageURL;

    @Enumerated(value = EnumType.STRING)
    private DiaryStatus parentStatus;

    @Enumerated(value = EnumType.STRING)
    private DiaryStatus childStatus;

    public enum DiaryStatus{
        RESPONSE,
        NO_RESPONSE
    };






    //  N(diary) : 1(hashtag)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="parentEmotionId")
    private EmotionHashtag childEmotionHashtag;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="childEmotionId")
    private EmotionHashtag parentEmotionHashtag;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="familyId")
    private Family family;



}
