package com.cmc.monggeul.domain.user.repository;

import com.cmc.monggeul.domain.user.entity.Role;
import com.cmc.monggeul.domain.user.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRoleCode(String roleCode);
}
