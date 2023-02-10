package com.cmc.monggeul.domain.user;

import com.cmc.monggeul.config.BaseException;
import com.cmc.monggeul.config.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cmc.monggeul.config.BaseResponseStatus.BAD_REQUEST;
import static com.cmc.monggeul.config.BaseResponseStatus.SUCCESS;

@RestController
public class UserController {
    @GetMapping("/")

    public ResponseEntity<BaseResponse> userTest(){
        try{
            return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
        } catch(BaseException exception){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(BAD_REQUEST));
        }

    }
    @GetMapping("/test")

    public ResponseEntity<BaseResponse> userTest2(){
        try{
            return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
        } catch(BaseException exception){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(BAD_REQUEST));
        }

    }
}
