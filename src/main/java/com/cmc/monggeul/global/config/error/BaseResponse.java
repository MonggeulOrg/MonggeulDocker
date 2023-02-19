package com.cmc.monggeul.global.config.error;

import com.cmc.monggeul.global.config.error.exception.BaseException;
import com.cmc.monggeul.global.config.error.exception.JwtException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cmc.monggeul.global.config.error.ErrorCode.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({ "code", "message", "result"})
public class BaseResponse<T>  {

    private final String message;

    private final int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    //성공 사
    public BaseResponse(T result) {

        this.code=SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.result = result;
    }

    public BaseResponse(JwtException e) {

        this.code=e.getCode();
        this.message = SUCCESS.getMessage();
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code=errorCode.getCode();
        this.message = errorCode.getMessage();
    }



    public BaseResponse(BaseException e) {

        this.code=e.getCode();
        this.message=e.getMessage();
    }


}