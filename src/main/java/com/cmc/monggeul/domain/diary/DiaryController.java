package com.cmc.monggeul.domain.diary;

import com.cmc.monggeul.domain.diary.dto.GetCategoryRes;
import com.cmc.monggeul.domain.diary.dto.GetQuestionRes;
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


    // [기록하기]

//    @PostMapping("/")
//    public ResponseEntity<BaseResponse<PostDiaryRes>>postDiary(@RequestBody PostDiaryReq postDiaryReq){
//
//    }


    // [기록하기] 이미지 업로드

}