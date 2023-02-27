package com.cmc.monggeul.domain.diary.controller;

import com.cmc.monggeul.domain.diary.dto.GetDateDto;
import com.cmc.monggeul.domain.diary.service.DiaryService;
import com.cmc.monggeul.domain.diary.service.HomeService;
import com.cmc.monggeul.global.config.error.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {


    private final HomeService homeService;


    // [홈] 디데이 계산기
    @GetMapping("/day/{familyId}")
    public ResponseEntity<BaseResponse<GetDateDto>> getDday(@PathVariable("familyId")Long familyId){
        GetDateDto getDateDto= homeService.getDday(familyId);

        return ResponseEntity.ok(new BaseResponse<>(getDateDto));

    }








}
