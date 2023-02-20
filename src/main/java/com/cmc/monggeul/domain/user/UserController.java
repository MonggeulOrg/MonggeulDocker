package com.cmc.monggeul.domain.user;

import com.cmc.monggeul.domain.user.dto.KakaoUserDto;
import com.cmc.monggeul.domain.user.dto.PostKakaoLoginReq;
import com.cmc.monggeul.domain.user.dto.PostKakaoLoginRes;
import com.cmc.monggeul.domain.user.service.UserService;
import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import com.cmc.monggeul.global.config.error.exception.JwtException;
import com.cmc.monggeul.global.config.oauth.kakao.KakaoService;
import com.cmc.monggeul.global.config.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    // 생성자 주입
    private final KakaoService kakaoService;
    private final UserService userService;

    // == 백엔드 카카오 로그인 테스트 ==
    @GetMapping("/test/kakao")
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


    // == 백엔드 애플 로그인 테스트 ==


    // 카카오 로그인
    @PostMapping("/kakao/login")
    public ResponseEntity<BaseResponse<PostKakaoLoginRes>> postKakaoLogin(@RequestBody PostKakaoLoginReq postKakaoLoginReq) {
        KakaoUserDto kakaoUserDto=kakaoService.createKakaoUser(postKakaoLoginReq.getKakaoAccessToken());
        PostKakaoLoginRes postKakaoLoginRes=userService.kakaoLogin(postKakaoLoginReq,kakaoUserDto);
        return ResponseEntity.ok(new BaseResponse<>(postKakaoLoginRes));
    }
//
    @GetMapping ("/hello")
    ResponseEntity<BaseResponse<String>>sayHi(){
        return  ResponseEntity.ok(new BaseResponse<>("Hi"));
    }

    // 구글 로그인

    //애플 로그인


    // 소셜 로그인 이후 유효성 검사만 진행한 다음 약관동의 페이지

    // 매칭 완료 -> 상대방 코드 입력 -> 상대방 코드 복호화 -> Matching 테이블에 넣을 것


    // 매칭 유효성 검사 (이미 매칭이 되었다면 유료 요금제로 전환하라고 알려줘야함)

}
