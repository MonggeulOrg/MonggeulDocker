package com.cmc.monggeul.domain.user.repository;

import com.cmc.monggeul.domain.user.entity.Term;
import com.cmc.monggeul.domain.user.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User>findByMatchingCode(String code);
}
