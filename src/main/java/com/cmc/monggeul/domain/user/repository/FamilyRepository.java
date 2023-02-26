package com.cmc.monggeul.domain.user.repository;

import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface FamilyRepository extends JpaRepository<Family,Long> {


    Family findByChild(Optional<User> child);

    Family findByParent(Optional<User> parent);

    Optional<Family> findById(Long id);
}
