package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Category extends BaseEntity {
    @Id
    private String code;

    private String name;

    private String subName;

    @OneToMany(mappedBy = "category")
    private List<RoleCategoryMapping> roleCategoryMappings = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Question> questions=new ArrayList<>();
}
