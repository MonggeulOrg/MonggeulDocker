package com.cmc.monggeul.domain.diary.controller;

import com.cmc.monggeul.domain.diary.dto.S3ImageDto;
import com.cmc.monggeul.domain.diary.service.S3Service;
import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.security.jwt.JwtAuthenticationFilter;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Api(tags = "이미지 업로드")
public class S3Controller {

    private final S3Service s3Service;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;


    @ApiOperation(
            value ="[Global] 이미지 업로드" )

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse<S3ImageDto>>uploadFile(@RequestParam MultipartFile multipartFile, HttpServletRequest httpServletRequest)
            throws IOException {
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        S3ImageDto s3ImageDto=s3Service.saveUploadFile(multipartFile,userEmail);
        return ResponseEntity.ok(new BaseResponse<>(s3ImageDto));

    }

}