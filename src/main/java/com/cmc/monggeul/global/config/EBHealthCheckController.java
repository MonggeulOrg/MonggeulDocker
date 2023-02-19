package com.cmc.monggeul.global.config;

import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cmc.monggeul.global.config.error.BaseResponseStatus.SUCCESS;

@RestController
public class EBHealthCheckController {
    @GetMapping("/")

    public ResponseEntity<BaseResponse> userTest(){
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));

    }
}