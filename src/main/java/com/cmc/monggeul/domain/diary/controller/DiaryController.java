package com.cmc.monggeul.domain.diary.controller;

import com.cmc.monggeul.domain.diary.dto.*;
import com.cmc.monggeul.domain.diary.entity.Category;
import com.cmc.monggeul.domain.diary.entity.Question;
import com.cmc.monggeul.domain.diary.repository.DiaryRepository;
import com.cmc.monggeul.domain.diary.repository.QuestionRepository;
import com.cmc.monggeul.domain.diary.service.DiaryService;
import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.security.jwt.JwtAuthenticationFilter;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/diary")
public class DiaryController{

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final DiaryService diaryService;



    // [기록하기] 유저별 카테고리 조회
    // 이메일을을 통해 유저정보를 받아온다 => 유저의 role을 가져온다.
    // familyId를 받아온다 => familyId에서 상대 유저의 role을 가져온다
    // 상대 유저와 유저의 교집합에 해당하는 카테고리를 반환한다.
    @GetMapping("/category/family/{familyId}")
    public ResponseEntity<BaseResponse<List<GetCategoryRes>>>getCategory(@PathVariable("familyId")Long familyId, HttpServletRequest httpServletRequest){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        List<GetCategoryRes> getCategoryRes= diaryService.getCategory(userEmail,familyId);
        return ResponseEntity.ok(new BaseResponse<>(getCategoryRes));
    }


    // [기록하기] 카테고리별 질문조회
    @GetMapping("/question/category")
    public ResponseEntity<BaseResponse<List<GetQuestionRes>>>getQuestion(@RequestParam("categoryCode")String categoryCode){
        List<GetQuestionRes> getQuestionRes=diaryService.getQuestion(categoryCode);
        return ResponseEntity.ok(new BaseResponse<>(getQuestionRes));

    }


    // [기록하기] 공유일기를 시작하는 경우
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<PostDiaryRes>>createDiary(@RequestBody PostDiaryReq postDiaryReq,HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        PostDiaryRes postDiaryRes=diaryService.createDiary(postDiaryReq,userEmail);
        return ResponseEntity.ok(new BaseResponse<>(postDiaryRes));

    }

    // [기록하기] 공유일기에 응답하는 경우
    @PostMapping("/response")
    public ResponseEntity<BaseResponse<PostDiaryRes>>postResponseDiary(@RequestBody PostResponseDiaryReq postResponseDiaryReq,HttpServletRequest request){

        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        PostDiaryRes postDiaryRes=diaryService.responseDiary(postResponseDiaryReq,userEmail);
        return ResponseEntity.ok(new BaseResponse<>(postDiaryRes));
    }

    // [홈,기록하기] 일기 상세조회

    @GetMapping("/{diaryId}")
    public ResponseEntity<BaseResponse<GetDiaryDetailRes>>getDiaryDetail(@PathVariable("diaryId")Long diaryId){

        GetDiaryDetailRes getDiaryDetailRes=diaryService.getDiaryDetail(diaryId);
        return ResponseEntity.ok(new BaseResponse<>(getDiaryDetailRes));
    }

    // [기록하기] 추억 확인하기
    @GetMapping("/question/family/{familyId}")
    public ResponseEntity<BaseResponse<List<GetConfirmQuestionRes>>> getConfirmQuestion(@PathVariable("familyId")Long familyId){
        List<GetConfirmQuestionRes> getConfirmQuestionResList=diaryService.getConfirmQuestion(familyId);
        return ResponseEntity.ok(new BaseResponse<>(getConfirmQuestionResList));
    }

    // [기록하기] 질문 추천 api
    @GetMapping("/question/recommend")
    public ResponseEntity<BaseResponse<List<GetQuestionRecommendRes>>>getRecommendQuestion(HttpServletRequest request){
        System.out.println("**");
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        List<GetQuestionRecommendRes> getQuestionRecommendResList=diaryService.getRecommendQuestion(userEmail);
        //System.out.println(getQuestionRecommendResList.get(0).getQuestionName());
        return ResponseEntity.ok(new BaseResponse<>(getQuestionRecommendResList));

    }





}