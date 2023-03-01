package com.cmc.monggeul.domain.alert.repository;

import com.cmc.monggeul.domain.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface AlertRepository extends JpaRepository<Alert,Long> {

    @Query("select a from Alert a where a.user.id=:userId and a.isRead=0 order by a.createdAt desc")
    List<Alert> findUserAlertRecent(Long userId);

    @Query("select a from Alert a where a.user.id=:userId and a.isRead=0 and a.diary.id=:diaryId")
    Alert findExistWaitAlert(Long userId, Long diaryId);
}
