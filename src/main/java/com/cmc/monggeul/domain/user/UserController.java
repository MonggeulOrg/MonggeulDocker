package com.cmc.monggeul.domain.user;

import com.cmc.monggeul.domain.user.dto.PostKakaoLoginReq;
import com.cmc.monggeul.domain.user.dto.PostKakaoLoginRes;
import com.cmc.monggeul.global.config.BaseException;
import com.cmc.monggeul.global.config.BaseResponse;
import com.cmc.monggeul.global.config.oauth.kakao.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    private final KakaoService kakaoService;

    public UserController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }
    //카카오 로그인 테스트

    @GetMapping("/test/kakao")
    public String  kakaoCallback(@RequestParam String code) throws BaseException {

        return kakaoService.getKaKaoAccessToken(code);

    }

    @GetMapping("/test/kakao/login")
    public void kakaoLoginTest(@RequestParam String token){
        kakaoService.createKakaoUser(token);

    }


    // 카카오 로그인
    @PostMapping("/kakao/login")
    public ResponseEntity<BaseResponse<PostKakaoLoginRes>>postKakaoLogin(@RequestBody PostKakaoLoginReq postKakaoLoginReq){

        return null;


    }


}
