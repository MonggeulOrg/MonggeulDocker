package com.cmc.monggeul.domain.community.entity;

import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1(κΈ) : N(chat)

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    // 1(μμ μ) : 1 (chat)
    @OneToOne
    private User to;

    @OneToOne
    private User from;


    @Lob
    private String text;
}
