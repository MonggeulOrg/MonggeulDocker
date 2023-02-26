package com.cmc.monggeul.domain.diary;

import com.cmc.monggeul.domain.diary.dto.GetDateDto;
import com.cmc.monggeul.domain.diary.service.DiaryService;
import com.cmc.monggeul.global.config.error.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiaryController {


    private final DiaryService diaryService;


    // [홈] 디데이 계산기
    @GetMapping("/home/day/{familyId}")
    public ResponseEntity<BaseResponse<GetDateDto>> getDday(@PathVariable("familyId")Long familyId){
        GetDateDto getDateDto= diaryService.getDday(familyId);

        return ResponseEntity.ok(new BaseResponse<>(getDateDto));

    }




}
