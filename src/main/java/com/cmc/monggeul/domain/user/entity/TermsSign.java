package com.cmc.monggeul.domain.user.entity;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
public class TermsSign extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Sign sign;

    @OneToMany(mappedBy = "termsSign")
    private List<Term> terms;

    public enum Sign{
        YES,
        NO
    }


}
