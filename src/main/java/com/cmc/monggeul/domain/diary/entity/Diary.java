package com.cmc.monggeul.domain.diary.entity;

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
    private String text;
    @Lob
    private String imageURL;



    //  N(diary) : 1(hashtag)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="emotionId")
    private EmotionHashtag emotionHashtag;

    // 1(diary) : N(sharing)
    @OneToMany(mappedBy = "diary")
    private List<UserDiarySharing> userDiarySharingList=new ArrayList<>();




}
