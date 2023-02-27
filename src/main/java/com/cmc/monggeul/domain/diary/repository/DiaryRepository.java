package com.cmc.monggeul.domain.diary.repository;

import com.cmc.monggeul.domain.diary.entity.Diary;
import com.cmc.monggeul.domain.diary.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary,Long> {
}
