package com.cmc.monggeul.global.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cmc.monggeul.global.config.BaseResponseStatus.BAD_REQUEST;
import static com.cmc.monggeul.global.config.BaseResponseStatus.SUCCESS;

@RestController
public class EBHealthCheckController {
    @GetMapping("/")

    public ResponseEntity<BaseResponse> userTest(){
        try{
            return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
        } catch(BaseException exception){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(BAD_REQUEST));
        }

    }
}
