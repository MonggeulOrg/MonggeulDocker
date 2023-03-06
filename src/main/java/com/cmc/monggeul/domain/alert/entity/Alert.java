package com.cmc.monggeul.domain.alert.entity;

import com.cmc.monggeul.domain.diary.entity.Diary;
import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Table(name="Alert",indexes = @Index(name="i_createdAt",columnList = "createdAt"))
public class Alert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저가 읽었는지 여부
    private int isRead;


    // 메세지 내용
    @Enumerated(value = EnumType.STRING)
    private MessageType messageType;

    public Alert() {

    }


    public enum MessageType{
        NEW_POST,
        COMPLETE,
        WAIT
    }

    // 알람(N) : 1(유저)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="senderId")
    private User sender;

    // 1(일기) : N(알람)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="diaryId")
    private Diary diary;

    // 1(가족) : N(알람)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="familyId")
    private Family family;

    public void updateRead(){
        this.isRead=1;
    }
}
