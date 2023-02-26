package com.cmc.monggeul.domain.diary.service;

import com.cmc.monggeul.domain.diary.dto.GetDateDto;
import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.repository.FamilyRepository;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class DiaryService {
    private final FamilyRepository familyRepository;

    public GetDateDto getDday(Long familyId){
        Optional<Family> family=familyRepository.findById(familyId);
        LocalDateTime startDateTime=family.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getCreatedAt();
        LocalDateTime endDateTime=LocalDateTime.now();
        int days= (int) ChronoUnit.DAYS.between(startDateTime,endDateTime);


        return GetDateDto.builder()
                .days(days)
                .build();
    }


}
