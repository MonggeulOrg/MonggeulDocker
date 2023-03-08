package com.cmc.monggeul.domain.diary.repository;

import com.cmc.monggeul.domain.diary.entity.Category;
import com.cmc.monggeul.domain.diary.entity.Question;
import com.cmc.monggeul.domain.user.entity.User;
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


    // [추천질문]
    // 유저가 답변하지 않은 질문에 대해서 조회
    // 유저가 응답대기 중이지 않은+응답한 질문에 대해서 조회
    // userQuestionMapping 에 해당하는 question이 아닌 경우만 추천
    //Todo UserQuestionMapping 쪽에서 유저 정보 추가
    // 딸 -> 엄마
    @Query("select q from Question q join Category c on q.category.code=c.code left outer join UserQuestionMapping  uq on uq.question.id=q.id and uq.user=:user  " +
            "where uq.question.id is null and  (q.category.code='MOM_DAU' or q.category.code='REMEMBER' or q.category.code='THINK' or q.category.code='YOUNG' ) "
            )
    List<Question>findMomDauRecQuestion(Optional<User> user);

    // 엄마 -> 딸
    @Query("select q from Question q join Category c on q.category.code=c.code left outer join UserQuestionMapping  uq on uq.question.id=q.id and uq.user=:user " +
            "where uq.question.id is null and  (q.category.code='DAU_MOM' or q.category.code='REMEMBER' or q.category.code='THINK' or q.category.code='YOUNG' )" )
    List<Question>findDauMomRecQuestion(Optional<User> user);

    // 아들 -> 엄마
    @Query("select q from Question q join Category c on q.category.code=c.code left outer join UserQuestionMapping  uq on uq.question.id=q.id and uq.user=:user "+
            "where uq.question.id is null and  (q.category.code='MOM_SON' or q.category.code='REMEMBER' or q.category.code='THINK' or q.category.code='YOUNG' )" )
    List<Question>findMomSonRecQuestion();

    // 엄마 -> 아들
    @Query("select q from Question q join Category c on q.category.code=c.code left outer join UserQuestionMapping  uq on uq.question.id=q.id and uq.user=:user " +
            "where uq.question.id is null and  (q.category.code='SON_MOM' or q.category.code='REMEMBER' or q.category.code='THINK' or q.category.code='YOUNG' ) "
     )
    List<Question>findSonMomRecQuestion();

    // 딸 -> 아빠
    @Query("select q from Question q join Category c on q.category.code=c.code left outer join UserQuestionMapping  uq on uq.question.id=q.id and uq.user=:user " +
            "where uq.question.id is null and  (q.category.code='DAD_DAU' or q.category.code='REMEMBER' or q.category.code='THINK' or q.category.code='YOUNG' ) " )
    List<Question> findDadDauRecQuestion();

    // 아빠 -> 딸
    @Query("select q from Question q join Category c on q.category.code=c.code left outer join UserQuestionMapping  uq on uq.question.id=q.id and uq.user=:user " +
            "where uq.question.id is null and  (q.category.code='DAU_DAD' or q.category.code='REMEMBER' or q.category.code='THINK' or q.category.code='YOUNG' ) " )
    List<Question> findDauDadRecQuestion();

    // 아들 -> 아빠
    @Query("select q from Question q join Category c on q.category.code=c.code left outer join UserQuestionMapping  uq on uq.question.id=q.id and uq.user=:user "+
            "where uq.question.id is null and  (q.category.code='DAD_SON' or q.category.code='REMEMBER' or q.category.code='THINK' or q.category.code='YOUNG' ) "
       )
    List<Question> findDadSonRecQuestion();

    //아빠 -> 아들
    @Query("select q from Question q join Category c on q.category.code=c.code left outer join UserQuestionMapping  uq on uq.question.id=q.id " +
            "where uq.question.id is null and  (q.category.code='SON_DAD' or q.category.code='REMEMBER' or q.category.code='THINK' or q.category.code='YOUNG' ) " )
    List<Question>findSonDadRecQuestion();

}
