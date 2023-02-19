package com.cmc.monggeul.global.config.error;

import com.cmc.monggeul.global.config.error.exception.BaseException;
import com.cmc.monggeul.global.config.error.exception.JwtException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.cmc.monggeul.global.config.error.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T>  {
    private final boolean isSuccess;
    private final String message;

    private final int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    //성공 사
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code=SUCCESS.getCode();
        this.result = result;
    }

    public BaseResponse(JwtException e) {
        this.isSuccess = false;
        this.code=e.getCode();
        this.message = e.getMessage();
    }

    public BaseResponse(ErrorCode errorCode) {
        this.isSuccess = false;
        this.message = errorCode.getErrorMessage();
        this.code=errorCode.getCode();
    }



    public BaseResponse(BaseException e) {
        this.isSuccess = false;
        this.code=e.getCode();
        this.message = e.getMessage();
    }


}