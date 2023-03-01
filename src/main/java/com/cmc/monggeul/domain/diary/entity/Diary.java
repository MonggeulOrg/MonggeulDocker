package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.domain.alert.entity.Alert;
import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@Getter
public class Diary extends BaseEntity {
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
    private DiaryStatus parentStatus=DiaryStatus.NO_RESPONSE;

    @Enumerated(value = EnumType.STRING)
    private DiaryStatus childStatus=DiaryStatus.NO_RESPONSE;

    public Diary() {

    }


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


    //Alert
    @OneToMany(mappedBy = "diary")
    private List<Alert> alertList=new ArrayList<>();

    public void updateChildInfo(String childText,String childImageURL,DiaryStatus childStatus,EmotionHashtag childEmotionHashtag){
        this.childText=childText;
        this.childImageURL=childImageURL;
        this.childStatus=childStatus;
        this.childEmotionHashtag=childEmotionHashtag;
    }


    public void updateParentInfo(String parentText,String parentImageURL,DiaryStatus parentStatus,EmotionHashtag parentEmotionHashtag){
        this.parentText=parentText;
        this.parentImageURL=parentImageURL;
        this.parentStatus=parentStatus;
        this.parentEmotionHashtag=parentEmotionHashtag;
    }




}
