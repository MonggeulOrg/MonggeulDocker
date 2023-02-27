package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    // 1(Category) : N(Question)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="categoryId")
    private Category category;

    @OneToMany(mappedBy = "question")
    private List<UserQuestionMapping> userQuestionMappings=new ArrayList<>();
}
