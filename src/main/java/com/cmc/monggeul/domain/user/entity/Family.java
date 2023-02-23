package com.cmc.monggeul.domain.user.entity;

import com.cmc.monggeul.domain.diary.entity.Diary;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Family extends BaseEntity {

    // 확장 가능성을 고려한 설계
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1(유저) : N(매칭 유저)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    private User child;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="parentId")
    private User parent;

    @OneToMany(mappedBy = "family")
    private List<Diary> diaryList=new ArrayList<>();

    @Builder
    public Family(User child,User parent){
        this.child=child;
        this.parent=parent;
    }





}
