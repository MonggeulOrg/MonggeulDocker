package com.cmc.monggeul.domain.diary.service;


import com.cmc.monggeul.domain.diary.dto.GetDateDto;
import com.cmc.monggeul.domain.diary.dto.GetRecentDiaryRes;
import com.cmc.monggeul.domain.diary.entity.Diary;
import com.cmc.monggeul.domain.diary.repository.DiaryRepository;
import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.domain.user.repository.FamilyRepository;
import com.cmc.monggeul.domain.user.repository.UserRepository;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class HomeService {

    private final UserRepository userRepository;

    private final FamilyRepository familyRepository;
    private final DiaryRepository diaryRepository;

    private final String MOM="MOM";
    private final String DAD="DAD";
    private final String SON="SON";

    private final String DAU="DAUGHTER";

    public GetDateDto getDday(Long familyId,String userEmail){
        Optional<User>user=userRepository.findByEmail(userEmail);
        String role=user.orElseThrow().getRole().getRoleCode();
        Optional<Family> family=familyRepository.findById(familyId);
        LocalDateTime startDateTime=family.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getCreatedAt();
        LocalDateTime endDateTime=LocalDateTime.now();
        int days= (int) ChronoUnit.DAYS.between(startDateTime,endDateTime);
        GetDateDto getDateDto;
        if(role.equals(MOM)||role.equals(DAD)){
            getDateDto=GetDateDto.builder()
                    .userRole(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getRole().getRoleCode())
                    .userProfileImg(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getProfileImgUrl())
                    .matchingUserProfileImg(family.orElseThrow().getChild().getProfileImgUrl())
                    .matchingUserRole(family.orElseThrow().getChild().getRole().getRoleCode())
                    .days(days)
                    .build();
        }else{
            getDateDto=GetDateDto.builder()
                    .userRole(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getRole().getRoleCode())
                    .userProfileImg(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getProfileImgUrl())
                    .matchingUserProfileImg(family.orElseThrow().getParent().getProfileImgUrl())
                    .matchingUserRole(family.orElseThrow().getParent().getRole().getRoleCode())
                    .days(days)
                    .build();

        }



        return getDateDto;
    }

    public List<GetRecentDiaryRes> getRecentDiary(String userEmail){
        Optional<User>user=userRepository.findByEmail(userEmail);
        String role=user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getRole().getRoleCode();
        List<Diary> diaryList;
        List<Diary> topFiveDiaryList=new ArrayList<>(5);
        List<GetRecentDiaryRes> getRecentDiaryResList=new ArrayList<>();
        Pageable pageable=PageRequest.of(0,5);
        if(role.equals(MOM)||role.equals(DAD)){

            diaryList=diaryRepository.findParentRecentPost(user.get().getId(),pageable);
            getRecentDiaryResList=diaryList.stream().map(
                    diary->GetRecentDiaryRes.builder()
                            .diaryId(diary.getId())
                            .questionName(diary.getQuestion().getName())
                            .categoryName(diary.getQuestion().getCategory().getName())
                            .imgUrl(diary.getParentImageURL())
                            .build()
            ).collect(Collectors.toList());



        }else if(role.equals(SON)||role.equals(DAU)){
            diaryList=diaryRepository.findChildRecentPost(user.get().getId(),pageable);
            getRecentDiaryResList=diaryList.stream().map(
                    diary->GetRecentDiaryRes.builder()
                            .diaryId(diary.getId())
                            .questionName(diary.getQuestion().getName())
                            .categoryName(diary.getQuestion().getCategory().getName())
                            .imgUrl(diary.getChildImageURL())
                            .build()
            ).collect(Collectors.toList());

        }
        return getRecentDiaryResList;

    }
}
