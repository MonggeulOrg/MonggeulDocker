package com.cmc.monggeul.domain.diary.repository;

import com.cmc.monggeul.domain.diary.entity.Question;
import com.cmc.monggeul.domain.diary.entity.UserQuestionMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserQuestionMappingRepository extends JpaRepository<UserQuestionMapping,Long> {

    @Query("select uq from UserQuestionMapping uq where uq.question.id=:questionId and uq.user.id=:userId")

    UserQuestionMapping findQuestionAndUser(Long questionId,Long userId);


}
