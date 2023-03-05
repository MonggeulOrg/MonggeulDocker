package com.cmc.monggeul.domain.diary.service;

import com.cmc.monggeul.domain.alert.entity.Alert;
import com.cmc.monggeul.domain.alert.repository.AlertRepository;
import com.cmc.monggeul.domain.diary.dto.*;
import com.cmc.monggeul.domain.diary.entity.*;
import com.cmc.monggeul.domain.diary.repository.*;
import com.cmc.monggeul.domain.user.dto.GetUserInfoByMatchingCodeRes;
import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.domain.user.repository.FamilyRepository;
import com.cmc.monggeul.domain.user.repository.UserRepository;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DiaryService {


    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final UserQuestionMappingRepository userQuestionMappingRepository;

    private final EmotionHashtagRepository emotionHashtagRepository;

    private final String MOM="MOM";
    private final String DAD="DAD";
    private final String SON="SON";

    private final String DAU="DAUGHTER";

    private final DiaryRepository diaryRepository;
    private final AlertRepository alertRepository;
    public List<GetCategoryRes> getCategory(String userEmail, Long familyId){
        Optional<User> user=userRepository.findByEmail(userEmail);
        Optional<Family> family=familyRepository.findById(familyId);
        Optional<User>matchingUser;
        List<Category> categoryList=new ArrayList<>();
        String role=user.get().getRole().getRoleCode();
        if(role.equals(MOM)){
            matchingUser=userRepository.findById(family.get().getChild().getId());
            if(matchingUser.get().getRole().getRoleCode().equals(SON)){
                categoryList=categoryRepository.findMomSonCategory();

            }else if(matchingUser.get().getRole().getRoleCode().equals(DAU)){
                categoryList=categoryRepository.findMomDauCategory();

            }

        }else if(role.equals(DAD)){
            matchingUser=userRepository.findById(family.get().getChild().getId());
            if(matchingUser.get().getRole().getRoleCode().equals(SON)){
                categoryList=categoryRepository.findDadSonCategory();

            }else if(matchingUser.get().getRole().getRoleCode().equals(DAU)){
                categoryList=categoryRepository.findDadDauCategory();

            }

        }else if(role.equals(SON)){
            matchingUser=userRepository.findById(family.get().getParent().getId());
            if(matchingUser.get().getRole().getRoleCode().equals(MOM)){
                categoryList=categoryRepository.findSonMomCategory();

            }else if(matchingUser.get().getRole().getRoleCode().equals(DAD)){
                categoryList=categoryRepository.findSonDadCategory();

            }


        }else if(role.equals(DAU)){
            matchingUser=userRepository.findById(family.get().getParent().getId());
            if(matchingUser.get().getRole().getRoleCode().equals(MOM)){
                categoryList=categoryRepository.findDauMomCategory();

            }else if(matchingUser.get().getRole().getRoleCode().equals(DAD)){
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
                        .categoryName(category.getName())
                        .subCategoryName(category.getSubName())
                        .questionId(question.getId())
                        .questionName(question.getName())
                        .build()
        ).collect(Collectors.toList());

        return getQuestionRes;


    }


    public PostDiaryRes createDiary(PostDiaryReq postDiaryReq,String userEmail){

        Optional<User> user=userRepository.findByEmail(userEmail);
        Optional<Family> family=familyRepository.findById(postDiaryReq.getFamilyId());
        Optional<Question> question=questionRepository.findById(postDiaryReq.getQuestionId());
        EmotionHashtag emotionHashtag=emotionHashtagRepository.findByText(postDiaryReq.getEmotionHashtag());
        Optional<EmotionHashtag> nullEmotion=emotionHashtagRepository.findById(6L);
        String role=user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getRole().getRoleCode();
        Diary diary = null;
        if(role.equals(MOM)||role.equals(DAD)){
            diary= diaryRepository.save(Diary.builder()
                    .question(question.orElseThrow(()->new RuntimeException("family가 존재하지 않습니다.")))
                    .family(family.orElseThrow(()->new RuntimeException("질문이 존재하지 않습니다.")))
                    .parentStatus(Diary.DiaryStatus.RESPONSE)
                    .parentText(postDiaryReq.getText())
                    .parentEmotionHashtag(emotionHashtag)
                    .parentImageURL(postDiaryReq.getImgUrl())
                    .childStatus(Diary.DiaryStatus.NO_RESPONSE)
                    .childText("")
                    .childImageURL("")
                    .childEmotionHashtag(nullEmotion.orElseThrow())
                    .build());


            // [알람] 자식 유저에게 새 글 작성 알람이 가게끔
            User child=family.get().getChild();
            alertRepository.save(Alert.builder()
                    .sender(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                    .user(child)
                    .family(family.orElseThrow(()->new RuntimeException("존재하지 않는 family입니다.")))
                    .isRead(0)
                    .messageType(Alert.MessageType.NEW_POST)
                    .diary(diary)
                    .build());


        }else if(role.equals(SON)||role.equals(DAU)){
            diary= diaryRepository.save(Diary.builder()
                    .question(question.orElseThrow())
                    .family(family.orElseThrow(()->new RuntimeException("질문이 존재하지 않습니다.")))
                    .childStatus(Diary.DiaryStatus.RESPONSE)
                    .childText(postDiaryReq.getText())
                    .childEmotionHashtag(emotionHashtag)
                    .childImageURL(postDiaryReq.getImgUrl())
                    .parentStatus(Diary.DiaryStatus.NO_RESPONSE)
                    .parentText("")
                    .parentImageURL("")
                    .parentEmotionHashtag(nullEmotion.orElseThrow())
                    .build());
            // [알람] 부모 유저에게 새 글 작성 알람이 가게끔
            User parent=family.get().getParent();
            alertRepository.save(Alert.builder()
                    .sender(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                    .user(parent)
                    .family(family.orElseThrow(()->new RuntimeException("존재하지 않는 family입니다.")))
                    .isRead(0)
                    .messageType(Alert.MessageType.NEW_POST)
                    .diary(diary)
                    .build());

        }

        // userQuestionMapping에 로그가 남게끔
        UserQuestionMapping userQuestionMapping=UserQuestionMapping.builder()
                .user(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                .question(question.orElseThrow(()->new BaseException(ErrorCode.QUESTION_NOT_EXIST)))
                .build();
        userQuestionMappingRepository.save(userQuestionMapping);


        PostDiaryRes postDiaryRes=PostDiaryRes.builder()
                .diaryId(diary.getId())
                .questionName(question.orElseThrow(()->new RuntimeException("질문이 존재하지 않습니다.")).getName())
                .categoryName(diary.getQuestion().getCategory().getName())
                .childDiaryText(diary.getChildText())
                .childEmotion(diary.getChildEmotionHashtag().getText())
                .childImgUrl(diary.getChildImageURL())
                .parentDiaryText(diary.getParentText())
                .parentImgUrl(diary.getParentImageURL())
                .parentEmotion(diary.getParentEmotionHashtag().getText())
                .build();

        return  postDiaryRes;
    }

    public PostDiaryRes responseDiary(PostResponseDiaryReq postResponseDiaryReq,String userEmail){
        Optional<User> user=userRepository.findByEmail(userEmail);
        Optional<Family>family=familyRepository.findById(postResponseDiaryReq.getFamilyId());
        EmotionHashtag emotionHashtag=emotionHashtagRepository.findByText(postResponseDiaryReq.getEmotionHashtag());
        String role=user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getRole().getRoleCode();
        Optional<Diary> diary=diaryRepository.findById(postResponseDiaryReq.getDiaryId());
        Question question=diary.orElseThrow(()->new BaseException(ErrorCode.QUESTION_NOT_EXIST)).getQuestion();
        if(role.equals(MOM)||role.equals(DAD)){
            diary.get().updateParentInfo(postResponseDiaryReq.getText(), postResponseDiaryReq.getImgUrl(), Diary.DiaryStatus.RESPONSE,emotionHashtag );
            // [알람] 자식 유저에게 글 완성 알람이 가게끔
            User child=family.get().getChild();
            Alert alert=alertRepository.save(Alert.builder()
                    .sender(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                    .user(child)
                    .family(family.orElseThrow(()->new RuntimeException("존재하지 않는 family입니다")))
                    .isRead(0)
                    .messageType(Alert.MessageType.COMPLETE)
                    .diary(diary.orElseThrow(()->new BaseException(ErrorCode.DIARY_NOT_EXIST)))
                    .build());
            System.out.println(alert.getId());

        }else if(role.equals(DAU)||role.equals(SON)){
            diary.get().updateChildInfo(postResponseDiaryReq.getText(), postResponseDiaryReq.getImgUrl(), Diary.DiaryStatus.RESPONSE,
                    emotionHashtag);

            // [알람] 부모 유저에게 글 완성 알람이 가게끔
            User parent=family.get().getParent();
            alertRepository.save(Alert.builder()
                    .sender(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                    .user(parent)
                    .family(family.orElseThrow(()->new RuntimeException("존재하지 않는 family입니다")))
                    .isRead(0)
                    .messageType(Alert.MessageType.COMPLETE)
                    .diary(diary.orElseThrow(()->new BaseException(ErrorCode.DIARY_NOT_EXIST)))
                    .build());
        }
        // userQuestionMapping에 로그가 남게끔
        UserQuestionMapping userQuestionMapping=UserQuestionMapping.builder()
                .user(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                .question(question)
                .answerStatus(UserQuestionMapping.QuestionStatus.YES)
                .build();
        userQuestionMappingRepository.save(userQuestionMapping);


        PostDiaryRes postDiaryRes=PostDiaryRes.builder()
                .diaryId(diary.orElseThrow().getId())
                .questionName(diary.orElseThrow().getQuestion().getName())
                .categoryName(diary.orElseThrow().getQuestion().getCategory().getName())
                .childDiaryText(diary.orElseThrow().getChildText())
                .childImgUrl(diary.orElseThrow().getChildImageURL())
                .childEmotion(diary.orElseThrow().getChildEmotionHashtag().getText()) // fetch join 할 것
                .childImgUrl(diary.orElseThrow().getChildImageURL())
                .parentDiaryText(diary.orElseThrow().getParentText())
                .parentImgUrl(diary.orElseThrow().getParentImageURL())
                .parentEmotion(diary.orElseThrow().getParentEmotionHashtag().getText())
                .build();

        return  postDiaryRes;



    }

    public GetDiaryDetailRes getDiaryDetail(Long diaryId){

        Optional<Diary> diary=diaryRepository.findById(diaryId);

        GetDiaryDetailRes getDiaryDetailRes=GetDiaryDetailRes.builder()
                .questionName(diary.orElseThrow().getQuestion().getName())
                .categoryName(diary.orElseThrow().getQuestion().getCategory().getName())
                .childDiaryText(diary.orElseThrow().getChildText())
                .childEmotion(diary.orElseThrow().getChildEmotionHashtag().getText()) // fetch join 할 것
                .parentDiaryText(diary.orElseThrow().getParentText())
                .parentEmotion(diary.orElseThrow().getParentEmotionHashtag().getText())
                .build();

        return  getDiaryDetailRes;

    }
    public List<GetConfirmQuestionRes> getConfirmQuestion(Long familyId){
        List<Optional<Diary>>diaryList=diaryRepository.findRecentQandA(familyId);
        List<GetConfirmQuestionRes>confirmQuestionRes=diaryList.stream().map(
                diary -> GetConfirmQuestionRes.builder()
                        .questionName(diary.orElseThrow(()->new BaseException(ErrorCode.DIARY_NOT_EXIST)).getQuestion().getName())
                        .categoryName(diary.orElseThrow(()->new BaseException(ErrorCode.DIARY_NOT_EXIST)).getQuestion().getCategory().getName())
                        .parentStatus(diary.orElseThrow(()->new BaseException(ErrorCode.DIARY_NOT_EXIST)).getParentStatus())
                        .childStatus(diary.orElseThrow(()->new BaseException(ErrorCode.DIARY_NOT_EXIST)).getChildStatus())
                        .createdAt(diary.orElseThrow(()->new BaseException(ErrorCode.DIARY_NOT_EXIST)).getCreatedAt()).build()
        ).collect(Collectors.toList());

        return confirmQuestionRes;

    }

    public List<GetQuestionRecommendRes> getRecommendQuestion(String userEmail){
        Optional<User>user=userRepository.findByEmail(userEmail);
        String role=user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getRole().getRoleCode();
        List<GetQuestionRecommendRes>questionRecommendResList=new ArrayList<>();

        if(role.equals(MOM)){
            Family family=familyRepository.findByParent(user);
            if(family.getChild().getRole().getRoleCode().equals(DAU)){
                List<Question> questions=questionRepository.findDauMomRecQuestion();
                questionRecommendResList=questions.stream().map(
                        question -> GetQuestionRecommendRes.builder()
                                .questionId(question.getId())
                                .questionName(question.getName())
                                .familyId(family.getId())
                                .build()
                ).collect(Collectors.toList());


            }else if(family.getChild().getRole().getRoleCode().equals(SON)){
                List<Question> questions=questionRepository.findSonMomRecQuestion();
                questionRecommendResList=questions.stream().map(
                        question -> GetQuestionRecommendRes.builder()
                                .questionId(question.getId())
                                .questionName(question.getName())
                                .familyId(family.getId())
                                .build()
                ).collect(Collectors.toList());

            }
        }else if(role.equals(DAD)){
            Family family=familyRepository.findByParent(user);

            if(family.getChild().getRole().getRoleCode().equals(DAU)){
                List<Question> questions=questionRepository.findDauDadRecQuestion();
                questionRecommendResList=questions.stream().map(
                        question -> GetQuestionRecommendRes.builder()
                                .questionId(question.getId())
                                .questionName(question.getName())
                                .familyId(family.getId())
                                .build()
                ).collect(Collectors.toList());


            }else if(family.getChild().getRole().getRoleCode().equals(SON)){
                List<Question> questions=questionRepository.findSonDadRecQuestion();
                questionRecommendResList=questions.stream().map(
                        question -> GetQuestionRecommendRes.builder()
                                .questionId(question.getId())
                                .questionName(question.getName())
                                .familyId(family.getId())
                                .build()
                ).collect(Collectors.toList());

            }

        }else if(role.equals(DAU)){
            Family family=familyRepository.findByChild(user);
            if(family.getParent().getRole().getRoleCode().equals(MOM)){
                System.out.println("***");
                List<Question> questions=questionRepository.findMomDauRecQuestion();
                questionRecommendResList=questions.stream().map(
                        question -> GetQuestionRecommendRes.builder()
                                .questionId(question.getId())
                                .questionName(question.getName())
                                .familyId(family.getId())
                                .build()
                ).collect(Collectors.toList());


            }else if(family.getParent().getRole().getRoleCode().equals(DAD)){
                List<Question> questions=questionRepository.findDadDauRecQuestion();
                questionRecommendResList=questions.stream().map(
                        question -> GetQuestionRecommendRes.builder()
                                .questionId(question.getId())
                                .questionName(question.getName())
                                .familyId(family.getId())
                                .build()
                ).collect(Collectors.toList());

            }

        }else if(role.equals(SON)){
            Family family=familyRepository.findByChild(user);
            if(family.getParent().getRole().getRoleCode().equals(MOM)){
                List<Question> questions=questionRepository.findMomSonRecQuestion();
                questionRecommendResList=questions.stream().map(
                        question -> GetQuestionRecommendRes.builder()
                                .questionId(question.getId())
                                .questionName(question.getName())
                                .familyId(family.getId())
                                .build()
                ).collect(Collectors.toList());


            }else if(family.getParent().getRole().getRoleCode().equals(DAD)){
                List<Question> questions=questionRepository.findDadSonRecQuestion();
                questionRecommendResList=questions.stream().map(
                        question -> GetQuestionRecommendRes.builder()
                                .questionId(question.getId())
                                .questionName(question.getName())
                                .familyId(family.getId())
                                .build()
                ).collect(Collectors.toList());

            }

        }


        return questionRecommendResList;




    }



}
