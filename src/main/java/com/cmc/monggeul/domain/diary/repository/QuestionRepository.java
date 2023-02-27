package com.cmc.monggeul.domain.diary.repository;

import com.cmc.monggeul.domain.diary.entity.Category;
import com.cmc.monggeul.domain.diary.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question>findByCategory(Category category);

    Optional<Question> findById(Long id);

}
