package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.domain.user.entity.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    // 1(user) : N(diary)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="parentId")
    private User parent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="childId")
    private User child;

    @Lob
    private String parentText;

    private String parentImgUrl;

    private String childImgUrl;

    @Lob
    private String childText;

    private DiaryStatus childDiaryStatus;

    private DiaryStatus parentDiaryStatus;


    //  N(diary) : 1(hashtag)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="parentEmotionId")
    private EmotionHashtag parentEmotionHashtag;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="childEmotionId")
    private EmotionHashtag childEmotionHashtag;

    enum DiaryStatus{

        RESPONSE,
        NO_RESPONSE
    }
}
