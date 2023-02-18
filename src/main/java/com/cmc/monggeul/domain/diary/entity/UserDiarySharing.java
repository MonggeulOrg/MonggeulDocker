package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.domain.user.entity.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class UserDiarySharing {

    // sharingId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="diaryId")
    private Diary diary;

    @Enumerated(value = EnumType.STRING)
    private DiaryStatus parentStatus;
    @Enumerated(value = EnumType.STRING)
    private DiaryStatus childStatus;

    enum DiaryStatus{

        RESPONSE,
        NONRESPONSE
    }


}
