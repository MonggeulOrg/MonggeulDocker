package com.cmc.monggeul.domain.diary.controller;

import com.cmc.monggeul.domain.diary.dto.GetDateDto;
import com.cmc.monggeul.domain.diary.dto.GetRecentDiaryRes;
import com.cmc.monggeul.domain.diary.service.DiaryService;
import com.cmc.monggeul.domain.diary.service.HomeService;
import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.security.jwt.JwtAuthenticationFilter;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {


    private final HomeService homeService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtTokenProvider jwtTokenProvider;


    // [홈] 디데이 계산기
    @GetMapping("/day/{familyId}")
    public ResponseEntity<BaseResponse<GetDateDto>> getDday(@PathVariable("familyId")Long familyId){
        GetDateDto getDateDto= homeService.getDday(familyId);

        return ResponseEntity.ok(new BaseResponse<>(getDateDto));

    }

    // [홈] 최근 쓴 일기 보기
    @GetMapping("/recent")
    public ResponseEntity<BaseResponse<List<GetRecentDiaryRes>>> getRecentDiary(HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        List<GetRecentDiaryRes> getRecentDiaryRes=homeService.getRecentDiary(userEmail);
        return ResponseEntity.ok(new BaseResponse<>(getRecentDiaryRes));
    }










}
