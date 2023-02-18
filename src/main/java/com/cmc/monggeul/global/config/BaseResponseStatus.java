package com.cmc.monggeul.global.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    SUCCESS(true, HttpStatus.OK, 200, "요청에 성공하였습니다."),
    BAD_REQUEST(false, HttpStatus.BAD_REQUEST, 400, "입력값을 확인해주세요."),
    FORBIDDEN(false, HttpStatus.FORBIDDEN, 403, "권한이 없습니다."),
    NOT_FOUND(false, HttpStatus.NOT_FOUND, 404, "대상을 찾을 수 없습니다."),

    // [User] 로그인

    TOKEN_NOT_EXIST(false,HttpStatus.UNAUTHORIZED, 403, "JWT Token이 존재하지 않습니다."),
    INVALID_TOKEN(false,HttpStatus.UNAUTHORIZED, 1002, "유효하지 않은 JWT Token 입니다."),
    ACCESS_TOKEN_EXPIRED(false,HttpStatus.UNAUTHORIZED, 1003, "만료된 Access Token 입니다."),
    REFRESH_TOKEN_EXPIRED(false,HttpStatus.UNAUTHORIZED, 1004, "만료된 Refresh Token 입니다."),

    EMAIL_ALREADY_EXIST(false,HttpStatus.BAD_REQUEST, 1005, "이미 존재하는 이메일입니다."),

    INVALID_KAKAO_ACCESS_TOKEN(false,HttpStatus.UNAUTHORIZED,1006,"유효하지 않은 Kakao Access Token입니다.");

    private final boolean isSuccess;
    @JsonIgnore
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, HttpStatus httpStatus, int code, String message) {
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
