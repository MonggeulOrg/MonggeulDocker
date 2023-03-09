package com.cmc.monggeul.domain.user;

import com.cmc.monggeul.domain.user.dto.*;
import com.cmc.monggeul.domain.user.dto.PostKakaoLoginReq;
import com.cmc.monggeul.domain.user.service.UserService;
import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import com.cmc.monggeul.global.config.error.exception.JwtException;
import com.cmc.monggeul.global.config.oauth.google.GoogleOAuth;
import com.cmc.monggeul.global.config.oauth.google.GoogleOAuthService;
import com.cmc.monggeul.global.config.oauth.google.GoogleOAuthToken;
import com.cmc.monggeul.global.config.oauth.kakao.KakaoService;
import com.cmc.monggeul.global.config.security.jwt.JwtAuthenticationFilter;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import com.cmc.monggeul.global.config.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    // 생성자 주입
    private final KakaoService kakaoService;

    private final GoogleOAuthService googleOAuthService;
    private final UserService userService;

    private final GoogleOAuth googleOAuth;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;


    // == 백엔드 카카오 로그인 테스트 ==
    @GetMapping("/test/kakao/code")
    public String  kakaoCallback(@RequestParam String code) throws BaseException {

        return kakaoService.getKaKaoAccessToken(code);

    }

    @GetMapping("/test/kakao/login")
    public ResponseEntity kakaoLoginTest(@RequestParam String token){
       try{
           return ResponseEntity.ok(kakaoService.createKakaoUser(token));
       }catch (BaseException e){
           return ResponseEntity.status(e.getCode()).body(e.getMessage());
       }catch(JwtException e){
           System.out.println(e.getMessage());
           return ResponseEntity.status(e.getCode()).body(e.getMessage());
       }

    }

    // == 백엔드 구글 로그인 테스트 ==

    @GetMapping("/test/google")
    public void socialLoginRedirect() throws IOException {
        System.out.println("auth");
        googleOAuthService.request();
    }

    @GetMapping("/test/google/code")
    public ResponseEntity<BaseResponse<GoogleOAuthToken>> testGoogleCallback(@RequestParam(name="code")String code) throws IOException, BaseException, ParseException, ParseException, org.json.simple.parser.ParseException {

        GoogleOAuthToken googleOAuthToken=googleOAuthService.oAuthLogin(code);
        return ResponseEntity.ok(new BaseResponse<>(googleOAuthToken));
    }

    @GetMapping("/test/google/access")
    public ResponseEntity<BaseResponse<GoogleUserDto>> testGoogleLogin(@RequestParam(name="accessToken")String accessToken) throws org.json.simple.parser.ParseException {

        return ResponseEntity.ok(new BaseResponse<>(googleOAuth.requestUserInfo(accessToken)));


    }



    // 카카오 로그인
    @PostMapping("/kakao/login")
    public ResponseEntity<BaseResponse<PostLoginRes>> postKakaoLogin(@RequestBody PostKakaoLoginReq postKakaoLoginReq) {
        KakaoUserDto kakaoUserDto=kakaoService.createKakaoUser(postKakaoLoginReq.getKakaoAccessToken());
        PostLoginRes postKakaoLoginRes=userService.kakaoLogin(postKakaoLoginReq,kakaoUserDto);
        return ResponseEntity.ok(new BaseResponse<>(postKakaoLoginRes));
    }

    // 구글 로그인

    @PostMapping("/google/login")
    public ResponseEntity<BaseResponse<PostLoginRes>> googleLogin(@RequestBody PostGoogleLoginReq postGoogleLoginReq) throws org.json.simple.parser.ParseException {

        GoogleUserDto googleUserDto=googleOAuth.requestUserInfo(postGoogleLoginReq.getGoogleAccessToken());
        PostLoginRes postLoginRes=userService.googleLogin(postGoogleLoginReq,googleUserDto);
        return ResponseEntity.ok(new BaseResponse<>(postLoginRes));

    }

    //애플 로그인
    @PostMapping("/apple/login")
    public ResponseEntity<BaseResponse<PostLoginRes>> appleLogin(@RequestBody PostAppleLoginReq postAppleLoginReq){

        PostLoginRes postLoginRes=userService.appleLogin(postAppleLoginReq);
        return ResponseEntity.ok(new BaseResponse<>(postLoginRes));

    }


    // 소셜 로그인 이후 유효성 검사만 진행한 다음 약관동의 페이지

    // 매칭 완료 -> 상대방 코드 입력 -> 상대방 코드 복호화 -> Matching 테이블에 넣을 것

    @PostMapping("/matching")
    public ResponseEntity<BaseResponse<PostMatchingRes>> matching(@RequestBody PostMatchingReq postMatchingReq, HttpServletRequest httpServletRequest){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        PostMatchingRes postMatchingRes=userService.matching(postMatchingReq.getMatchingUserCode(),userEmail);
        return ResponseEntity.ok(new BaseResponse<>(postMatchingRes));


    }

    // 매칭코드,familyId 조회
    @GetMapping("/matching")
    public ResponseEntity<BaseResponse<GetUserMatchingCodeRes>> getMatchingCode(HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        GetUserMatchingCodeRes getUserMatchingCodeRes=userService.getMatchingCode(userEmail);
        return ResponseEntity.ok(new BaseResponse<>(getUserMatchingCodeRes));

    }

    //매칭코드에 대한 유저정보 조회
    @GetMapping("/matching/{matchingCode}")
    public ResponseEntity<BaseResponse<GetUserInfoByMatchingCodeRes>>getUserInfoByMatchingCode(@PathVariable("matchingCode")String matchingCode){


        GetUserInfoByMatchingCodeRes getUserInfoByMatchingCodeRes=userService.getUserInfoByMatchingCode(matchingCode);
        return ResponseEntity.ok(new BaseResponse<>(getUserInfoByMatchingCodeRes));

    }

    // [마이페이지] 유저정보 조회
    @GetMapping("/mypage")
    public ResponseEntity<BaseResponse<GetUserMyPageDto>> getUserMyPage(HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        GetUserMyPageDto getUserMyPageDto=userService.getUserMyPage(userEmail);
        return ResponseEntity.ok(new BaseResponse<>(getUserMyPageDto));

    }


    // [마이페이지] 회원 정보 수정
    @PutMapping("/mypage")
    public ResponseEntity<BaseResponse<GetUserMyPageDto>>patchMyPage(@RequestBody PatchUserMyPageDto patchUserMyPageDto, HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        GetUserMyPageDto getUserMyPageDto=userService.patchMyPage(userEmail,patchUserMyPageDto);
        return ResponseEntity.ok(new BaseResponse<>(getUserMyPageDto));

    }



    // [마이페이지] 회원 탈퇴하기

    @PutMapping("/delete")
    public ResponseEntity<BaseResponse<GetUserMyPageDto>>deleteUser(HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        GetUserMyPageDto getUserMyPageDto=userService.deleteUser(userEmail);
        return ResponseEntity.ok(new BaseResponse<>(getUserMyPageDto));

    }

    // [마이페이지] 유저 활성화 상태확인

    @GetMapping("/status")
    public ResponseEntity<BaseResponse<GetUserStatus>>getUserStatus(HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        GetUserStatus getUserStatus=userService.getUserStatus(userEmail);
        return ResponseEntity.ok(new BaseResponse<>(getUserStatus));

    }

    // [마이페이지] 가족 연결 끊기

    // [마이페이지] 가족 연결 상태 확인

    //





}
