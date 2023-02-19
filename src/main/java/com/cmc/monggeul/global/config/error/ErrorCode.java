package com.cmc.monggeul.global.config.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@Getter

public enum ErrorCode {

    SUCCESS(HttpStatus.OK,200,  "요청에 성공하였습니다."),
    BAD_REQUEST( HttpStatus.BAD_REQUEST,400,  "입력값을 확인해주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN,  403,"권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404,"대상을 찾을 수 없습니다."),

    // [User] 로그인

    TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, 403, "JWT Token이 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,  403,"유효하지 않은 JWT Token 입니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,  403,"만료된 Access Token 입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,  403,"만료된 Refresh Token 입니다."),
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED,  403,"사용자 인증에 실패하였습니다."),

    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, 500,"이미 존재하는 이메일입니다."),

    INVALID_KAKAO_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,403,"유효하지 않은 Kakao Access Token입니다."),
    INVALID_HTTP_METHOD(METHOD_NOT_ALLOWED, 3001, "잘못된 Http Method 요청입니다."),
    INVALID_VALUE(HttpStatus.BAD_REQUEST, 3002, "잘못된 입력값입니다."),
    SERVER_INTERNAL_ERROR(INTERNAL_SERVER_ERROR, 3003, "서버 내부에 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,403,"인가되지 않은 사용자입니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String errorMessage;

   ErrorCode( HttpStatus httpStatus, int code, String errorMessage) {

        this.httpStatus = httpStatus;
        this.code = code;
        this.errorMessage = errorMessage;
    }


}
