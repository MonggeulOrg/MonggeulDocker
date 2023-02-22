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
import com.cmc.monggeul.global.config.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BaseResponse<GoogleOAuthToken>> callback(@RequestParam(name="code")String code) throws IOException, BaseException, ParseException, ParseException, org.json.simple.parser.ParseException {

        GoogleOAuthToken googleOAuthToken=googleOAuthService.oAuthLogin(code);
        return ResponseEntity.ok(new BaseResponse<>(googleOAuthToken));
    }

    @GetMapping("/test/google/access")
    public ResponseEntity<BaseResponse<GoogleUserDto>> login(@RequestParam(name="accessToken")String accessToken) throws org.json.simple.parser.ParseException {

        return ResponseEntity.ok(new BaseResponse<>(googleOAuth.requestUserInfo(accessToken)));


    }

    // == 백엔드 애플 로그인 테스트 ==


    // 카카오 로그인
    @PostMapping("/kakao/login")
    public ResponseEntity<BaseResponse<PostLoginRes>> postKakaoLogin(@RequestBody PostKakaoLoginReq postKakaoLoginReq) {
        KakaoUserDto kakaoUserDto=kakaoService.createKakaoUser(postKakaoLoginReq.getKakaoAccessToken());
        PostLoginRes postKakaoLoginRes=userService.kakaoLogin(postKakaoLoginReq,kakaoUserDto);
        return ResponseEntity.ok(new BaseResponse<>(postKakaoLoginRes));
    }

    // 구글 로그인

    @PostMapping("/google/login")
    public ResponseEntity<BaseResponse<PostLoginRes>> login(@RequestBody PostGoogleLoginReq postGoogleLoginReq) throws org.json.simple.parser.ParseException {

        GoogleUserDto googleUserDto=googleOAuth.requestUserInfo(postGoogleLoginReq.getGoogleAccessToken());
        PostLoginRes postLoginRes=userService.googleLogin(postGoogleLoginReq,googleUserDto);
        return ResponseEntity.ok(new BaseResponse<>(postLoginRes));

    }

    //애플 로그인


    // 소셜 로그인 이후 유효성 검사만 진행한 다음 약관동의 페이지

    // 매칭 완료 -> 상대방 코드 입력 -> 상대방 코드 복호화 -> Matching 테이블에 넣을 것


    // 매칭 유효성 검사 (이미 매칭이 되었다면 유료 요금제로 전환하라고 알려줘야함)

}
