package com.cmc.monggeul.domain.user.entity;

import com.cmc.monggeul.domain.diary.entity.RoleCategoryMapping;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
public class Role extends BaseEntity {

    @Id
    private String roleCode;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @OneToMany(mappedBy = "role")
    private List<RoleCategoryMapping> roleCategoryMappings = new ArrayList<>();

    public Role() {

    }
}
