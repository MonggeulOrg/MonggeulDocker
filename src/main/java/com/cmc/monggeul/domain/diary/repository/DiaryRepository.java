package com.cmc.monggeul.domain.diary.repository;

import com.cmc.monggeul.domain.diary.entity.Category;
import com.cmc.monggeul.domain.diary.entity.Diary;
import com.cmc.monggeul.domain.diary.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    @Query(value = "select d from Diary d where d.family.parent.id=:parentId and d.parentStatus='RESPONSE' order by d.createdAt desc")
    List<Diary>findParentRecentPost(Long parentId);

    @Query(value = "select d from Diary d where d.family.child.id=:childId and d.childStatus='RESPONSE' order by d.createdAt desc")
    List<Diary>findChildRecentPost(Long childId);

    List<Diary>findByFamilyId(Long familyId);

    @Query("select d from Diary  d where d.family.id=:familyId order by d.createdAt desc")
    List<Optional<Diary>>findRecentQandA(Long familyId);


}
