package com.cmc.monggeul.domain.diary.entity;

import com.cmc.monggeul.domain.user.entity.Role;
import com.cmc.monggeul.global.config.BaseEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class RoleCategoryMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1(Role) : N(categoru)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="roleCode")
    private Role role;

    // 1(category) : N(role)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="categoryCode")
    private Category category;




}
