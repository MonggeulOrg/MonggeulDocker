package com.cmc.monggeul.domain.community.entity;

import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 커뮤니티 코드
    // 1(커뮤니티) : N(글)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "communityCategoryId")
    private CommunityCategory communityCategory;

    private String title;


    @Lob
    private String text;

    // 글 작성자 1(유저) : N (글)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writerId")
    private User writer;

    // 답변자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewerId")
    private User reviewer;

    @OneToMany
    private List<Chat> chatList=new ArrayList<>();
}
