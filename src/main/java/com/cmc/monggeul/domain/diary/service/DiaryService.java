package com.cmc.monggeul.domain.diary.service;

import com.cmc.monggeul.domain.diary.dto.GetCategoryRes;
import com.cmc.monggeul.domain.diary.dto.GetDateDto;
import com.cmc.monggeul.domain.diary.dto.GetQuestionRes;
import com.cmc.monggeul.domain.diary.entity.Category;
import com.cmc.monggeul.domain.diary.entity.Question;
import com.cmc.monggeul.domain.diary.repository.CategoryRepository;
import com.cmc.monggeul.domain.diary.repository.QuestionRepository;
import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.domain.user.repository.FamilyRepository;
import com.cmc.monggeul.domain.user.repository.UserRepository;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class DiaryService {


    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    public List<GetCategoryRes> getCategory(String userEmail, Long familyId){
        Optional<User> user=userRepository.findByEmail(userEmail);
        Optional<Family> family=familyRepository.findById(familyId);
        Optional<User>matchingUser;
        List<Category> categoryList=new ArrayList<>();
        String role=user.get().getRole().getRoleCode();
        if(role.equals("MOM")){
            matchingUser=userRepository.findById(family.get().getChild().getId());
            if(matchingUser.get().getRole().getRoleCode().equals("SON")){
                categoryList=categoryRepository.findMomSonCategory();

            }else if(matchingUser.get().getRole().getRoleCode().equals("DAUGHTER")){
                categoryList=categoryRepository.findMomDauCategory();

            }

        }else if(role.equals("DAD")){
            matchingUser=userRepository.findById(family.get().getChild().getId());
            if(matchingUser.get().getRole().getRoleCode().equals("SON")){
                categoryList=categoryRepository.findDadSonCategory();

            }else if(matchingUser.get().getRole().getRoleCode().equals("DAUGHTER")){
                categoryList=categoryRepository.findDadDauCategory();

            }

        }else if(role.equals("SON")){
            matchingUser=userRepository.findById(family.get().getParent().getId());
            if(matchingUser.get().getRole().getRoleCode().equals("MOM")){
                categoryList=categoryRepository.findSonMomCategory();

            }else if(matchingUser.get().getRole().getRoleCode().equals("DAD")){
                categoryList=categoryRepository.findSonDadCategory();

            }


        }else if(role.equals("DAUGHTER")){
            matchingUser=userRepository.findById(family.get().getParent().getId());
            if(matchingUser.get().getRole().getRoleCode().equals("MOM")){
                categoryList=categoryRepository.findDauMomCategory();

            }else if(matchingUser.get().getRole().getRoleCode().equals("DAD")){
                categoryList=categoryRepository.findDauMomCategory();

            }


        }
        List<GetCategoryRes> getCategoryRes = categoryList.stream().map(
                    category -> GetCategoryRes.builder()
                            .categoryCode(category.getCode())
                            .categoryName(category.getName())
                            .subName(category.getSubName()).build()).collect(Collectors.toList());

        return getCategoryRes;

    }

    public List<GetQuestionRes> getQuestion(String categoryCode){
        Category category=categoryRepository.findByCode(categoryCode);
        List<Question>questionList=questionRepository.findByCategory(category);
        List<GetQuestionRes> getQuestionRes=questionList.stream().map(
                question -> GetQuestionRes.builder()
                        .questionId(question.getId())
                        .questionName(question.getName())
                        .build()
        ).collect(Collectors.toList());

        return getQuestionRes;


    }


}
