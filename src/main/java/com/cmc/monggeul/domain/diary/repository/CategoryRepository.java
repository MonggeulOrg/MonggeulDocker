package com.cmc.monggeul.domain.diary.repository;

import com.cmc.monggeul.domain.diary.entity.Category;
import com.cmc.monggeul.domain.diary.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<Category,String> {

    Category findByCode(String code);

    @Query(value = "select * from Category as c where (c.code like 'MOM_SON') or (c.code like 'YOUNG')" +
            "or (c.code like 'REMEMBER') or (c.code like 'THINK')",nativeQuery = true)
    List<Category> findMomSonCategory();

    @Query(value = "select * from Category as c where (c.code like 'MOM_SON') or (c.code like 'YOUNG')" +
            "or (c.code like 'REMEMBER') or (c.code like 'THINK')",nativeQuery = true)
    List<Category> findMomDauCategory();

    @Query(value = "select * from Category as c where (c.code like 'MOM_SON') or (c.code like 'YOUNG')" +
            "or (c.code like 'REMEMBER') or (c.code like 'THINK')",nativeQuery = true)
    List<Category> findDadSonCategory();

    @Query(value = "select * from Category as c where (c.code like 'MOM_SON') or (c.code like 'YOUNG')" +
            "or (c.code like 'REMEMBER') or (c.code like 'THINK')",nativeQuery = true)
    List<Category> findDadDauCategory();

    @Query(value = "select * from Category as c where (c.code like 'SON_MOM') or (c.code like 'YOUNG')" +
            "or (c.code like 'REMEMBER') or (c.code like 'THINK')",nativeQuery = true)
    List<Category> findDauMomCategory();

    @Query(value = "select * from Category as c where (c.code like 'SON_MOM') or (c.code like 'YOUNG')" +
            "or (c.code like 'REMEMBER') or (c.code like 'THINK')",nativeQuery = true)
    List<Category> findDauDadCategory();

    @Query(value = "select * from Category as c where (c.code like 'SON_MOM') or (c.code like 'YOUNG')" +
            "or (c.code like 'REMEMBER') or (c.code like 'THINK')",nativeQuery = true)
    List<Category> findSonMomCategory();

    @Query(value = "select * from Category as c where (c.code like 'SON_MOM') or (c.code like 'YOUNG')" +
            "or (c.code like 'REMEMBER') or (c.code like 'THINK')",nativeQuery = true)
    List<Category> findSonDadCategory();


}
