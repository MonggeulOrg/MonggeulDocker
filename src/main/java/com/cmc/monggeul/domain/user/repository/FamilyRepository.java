package com.cmc.monggeul.domain.user.repository;

import com.cmc.monggeul.domain.user.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface FamilyRepository extends JpaRepository<Family,Long> {
}
