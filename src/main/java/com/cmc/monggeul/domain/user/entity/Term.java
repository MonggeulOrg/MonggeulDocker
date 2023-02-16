package com.cmc.monggeul.domain.user.entity;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Term extends BaseEntity {

    @Id
    private String code;

    @Column
    private String title;

    @Column
    private String content;

    // N(약관) : 1(약관동의)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="termsSignId")
    private TermsSign termsSign;

    @Enumerated(value = EnumType.STRING)
    private Optional optional;

    public enum Optional{

        YES,
        NO
    }



}
