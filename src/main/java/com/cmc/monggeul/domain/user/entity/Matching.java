package com.cmc.monggeul.domain.user.entity;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Matching extends BaseEntity {

    // 확장 가능성을 고려한 설계
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1(유저) : N(매칭 유저)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="matchingUserId")
    private User matchingUser;





}
