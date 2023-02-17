package com.cmc.monggeul.domain.user.repository;

import com.cmc.monggeul.domain.user.entity.Term;
import com.cmc.monggeul.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
}
