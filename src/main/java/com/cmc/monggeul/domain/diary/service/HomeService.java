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

    public GetDateDto getDday(Long familyId){
        Optional<Family> family=familyRepository.findById(familyId);
        LocalDateTime startDateTime=family.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getCreatedAt();
        LocalDateTime endDateTime=LocalDateTime.now();
        int days= (int) ChronoUnit.DAYS.between(startDateTime,endDateTime);


        return GetDateDto.builder()
                .days(days)
                .build();
    }

    public List<GetRecentDiaryRes> getRecentDiary(String userEmail){
        Optional<User>user=userRepository.findByEmail(userEmail);
        String role=user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getRole().getRoleCode();
        List<Diary> diaryList=new ArrayList<>();
        List<GetRecentDiaryRes> getRecentDiaryResList=new ArrayList<>();
        if(role.equals(MOM)||role.equals(DAD)){
            diaryList=diaryRepository.findParentRecentPost(user.get().getId());
            getRecentDiaryResList=diaryList.stream().map(
                    diary->GetRecentDiaryRes.builder()
                            .diaryId(diary.getId())
                            .questionName(diary.getQuestion().getName())
                            .categoryName(diary.getQuestion().getCategory().getName())
                            .imgUrl(diary.getParentImageURL())
                            .build()
            ).collect(Collectors.toList());


        }else if(role.equals(SON)||role.equals(DAU)){
            diaryList=diaryRepository.findChildRecentPost(user.get().getId());
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
