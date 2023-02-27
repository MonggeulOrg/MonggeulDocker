package com.cmc.monggeul.domain.diary.repository;

import com.cmc.monggeul.domain.diary.entity.EmotionHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface EmotionHashtagRepository extends JpaRepository<EmotionHashtag,Long> {

    EmotionHashtag findByText(String text);
}
