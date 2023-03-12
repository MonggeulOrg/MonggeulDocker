package com.cmc.monggeul.domain.diary.repository;

import com.cmc.monggeul.domain.diary.entity.Category;
import com.cmc.monggeul.domain.diary.entity.Diary;
import com.cmc.monggeul.domain.diary.entity.Question;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    @Query(value = "select d from Diary d where d.family.parent.id=:parentId and d.parentStatus='RESPONSE' order by d.createdAt desc ")
    List<Diary>findParentRecentPost(@Param("parentId")Long parentId, Pageable pageable);

    @Query(value = "select d from Diary d where d.family.child.id=:childId and d.childStatus='RESPONSE' order by d.createdAt desc")
    List<Diary>findChildRecentPost(Long childId,Pageable pageable);

    List<Diary>findByFamilyId(Long familyId);

    @Query("select d from Diary  d where d.family.id=:familyId order by d.createdAt desc")
    List<Optional<Diary>>findRecentQandA(Long familyId);

    @Query(value = "select * from Diary as d  join Family as f on f.id=d.familyId  where f.parentId=:parentId and d.parentStatus='NO_RESPONSE' and TIMESTAMPDIFF(DAY,d.createdAt,CURRENT_DATE())>=5 ",nativeQuery = true)
    List<Diary>parentNotResponseMoreThanFive(@Param("parentId")Long parentId);

    @Query(value = "select * from Diary as  d join Family as f on f.id=d.familyId where f.userId=:childId and d.childStatus='NO_RESPONSE' and TIMESTAMPDIFF(DAY,d.createdAt,CURRENT_DATE())>=5",nativeQuery = true)
    List<Diary>childNotResponseMoreThanFive(@Param("childId")Long childId);

    @Query("select count(d.id) from Diary d where d.family.id=:familyId")
    int getDiaryCount(@Param("familyId")Long familyId);



}
