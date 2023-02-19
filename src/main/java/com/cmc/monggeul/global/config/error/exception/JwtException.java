package com.cmc.monggeul.global.config.error.exception;

import com.cmc.monggeul.global.config.error.ErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {

    private final ErrorCode jwtErrorCode;
    private final int code;
    private final String errorMessage;

    // Without Cause Exception
    public JwtException(ErrorCode errorcode) {
        super(errorcode.getErrorMessage());
        this.jwtErrorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorcode.getErrorMessage();
    }

    public JwtException(ErrorCode errorcode, String errorMessage) {
        super(errorMessage);
        this.jwtErrorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorMessage;
    }

    // With Cause Exception
    public JwtException(ErrorCode errorcode, Exception cause) {
        super(errorcode.getErrorMessage(), cause);
        this.jwtErrorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorcode.getErrorMessage();
    }

    public JwtException(ErrorCode errorcode, String errorMessage, Exception cause) {
        super(errorMessage, cause);
        this.jwtErrorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorMessage;
    }

}