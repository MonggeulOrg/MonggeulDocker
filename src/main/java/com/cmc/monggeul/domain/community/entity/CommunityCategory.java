package com.cmc.monggeul.domain.community.entity;

import com.cmc.monggeul.global.config.BaseEntity;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class CommunityCategory extends BaseEntity {
    @Id
    private String code;
    private String name;
}
